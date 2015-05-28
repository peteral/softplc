package de.peteral.softplc.plc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import javax.script.ScriptEngineManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.peteral.softplc.address.AddressParserFactory;
import de.peteral.softplc.comm.PutGetServerImpl;
import de.peteral.softplc.cpu.CpuImpl;
import de.peteral.softplc.cpu.ErrorLogImpl;
import de.peteral.softplc.datatype.DataTypeFactory;
import de.peteral.softplc.executor.ScheduledThreadPoolExecutorFactory;
import de.peteral.softplc.memory.MemoryAreaImpl;
import de.peteral.softplc.memory.MemoryImpl;
import de.peteral.softplc.memorytables.MemoryTable;
import de.peteral.softplc.memorytables.MemoryTableVariable;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.Memory;
import de.peteral.softplc.model.MemoryArea;
import de.peteral.softplc.model.Plc;
import de.peteral.softplc.model.Program;
import de.peteral.softplc.program.Precompiler;
import de.peteral.softplc.program.ProgramImpl;
import de.peteral.softplc.program.ScriptFile;

/**
 * Creates a {@link Plc} instance from configuration file.
 *
 * @author peteral
 */
public class PlcFactory {
	private final Logger logger = Logger.getLogger("PlcFactory");

	private static final int PORT = 102;
	private static final int DEFAULT_MAX_CONNECTIONS = 16;
	private static final int DEFAULT_MAX_BLOCK_SIZE = 222;
	private static final Map<String, Integer> DEFAULT_MEMORY_AREAS = new HashMap<>();

	private static final long DEFAULT_CYCLE_TIME = 50;

	static {
		DEFAULT_MEMORY_AREAS.put("M", 65535);
		DEFAULT_MEMORY_AREAS.put("I", 65535);
		DEFAULT_MEMORY_AREAS.put("O", 65535);
	}

	/**
	 * Creates {@link Plc} instance based on the configuration file.
	 *
	 * @param path
	 *            path to the configuration file
	 * @return configured {@link Plc} instance
	 */
	public Plc create(String path) {
		try {
			byte[] contents = Files.readAllBytes(Paths.get(path));
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(contents));

			return createFromDocument(doc, path);
		} catch (IOException | ParserConfigurationException | SAXException
				| URISyntaxException e) {
			throw new PlcFactoryException(path, e);
		}
	}

	/**
	 * Creates new empty CPU for given PLC. Looks up next free CPU number
	 *
	 * @param plc
	 * @return new CPU instance
	 */
	public Cpu createCpu(Plc plc) {

		final List<MemoryArea> memoryAreas = new ArrayList<>();

		DEFAULT_MEMORY_AREAS.entrySet().forEach(
				entry -> memoryAreas.add(new MemoryAreaImpl(entry.getKey(),
						entry.getValue(), true)));

		Cpu result = new CpuImpl("New PLC", getFreeSlot(plc),
				new ErrorLogImpl(), new ScheduledThreadPoolExecutorFactory(),
				new MemoryImpl(new AddressParserFactory(),
						new DataTypeFactory(), memoryAreas
								.toArray(new MemoryArea[memoryAreas.size()])),
				DEFAULT_MAX_BLOCK_SIZE, DEFAULT_MAX_CONNECTIONS);

		result.loadProgram(new ProgramImpl(result, new ScriptEngineManager(),
				new Precompiler(), DEFAULT_CYCLE_TIME));

		return result;
	}

	private int getFreeSlot(Plc plc) {
		final IntegerProperty highestSlot = new SimpleIntegerProperty();

		plc.getCpus().forEach(
				cpu -> highestSlot.set(Math.max(highestSlot.get(), cpu
						.getSlot().get())));

		return highestSlot.get() + 1;
	}

	private Plc createFromDocument(Document doc, String path)
			throws IOException, ParserConfigurationException, SAXException,
			URISyntaxException {
		logger.info("Parsing file: " + path);
		List<Cpu> cpus = new ArrayList<>();

		// process includes
		NodeList includeElements = doc.getElementsByTagName("includes");
		for (int i = 0; i < includeElements.getLength(); i++) {
			Element includeElement = (Element) includeElements.item(i);
			URI includeUri = new File(path).getParentFile().toURI()
					.resolve(includeElement.getAttribute("file"));
			String includePath = new File(includeUri).getAbsolutePath();
			byte[] contents = Files.readAllBytes(Paths.get(includePath));
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document includeDoc = builder.parse(new ByteArrayInputStream(
					contents));

			Plc plc = createFromDocument(includeDoc, includePath);

			for (Cpu cpu : plc.getCpus()) {
				cpus.add(cpu);
			}
		}

		NodeList cpuElements = doc.getElementsByTagName("cpu");
		for (int i = 0; i < cpuElements.getLength(); i++) {
			Element cpuElement = (Element) cpuElements.item(i);

			cpus.add(createCpu(cpuElement, path));
		}

		return new PlcImpl(new PutGetServerImpl(PORT),
				cpus.toArray(new Cpu[cpus.size()]));
	}

	private Cpu createCpu(Element cpuElement, String path) throws IOException,
			DOMException, URISyntaxException {
		int slot = Integer.parseInt(cpuElement.getAttribute("slot"));

		Memory memory = createMemory(cpuElement);
		int maxBlockSize = Integer.parseInt(cpuElement
				.getAttribute("maxBlockSize"));
		int maxConnections = Integer.parseInt(cpuElement
				.getAttribute("connections"));

		Cpu cpu = new CpuImpl(cpuElement.getAttribute("name"), slot,
				new ErrorLogImpl(), new ScheduledThreadPoolExecutorFactory(),
				memory, maxBlockSize, maxConnections);

		createTables(cpu, cpuElement);

		Program program = createProgram(cpuElement, cpu, path);

		cpu.loadProgram(program);

		return cpu;
	}

	private void createTables(Cpu cpu, Element cpuElement) {
		List<Element> tablesElements = getChildrenByName(cpuElement, "tables");
		if (tablesElements.size() == 0) {
			return;
		}

		tablesElements.forEach(tablesElement -> {
			List<Element> tableElements = getChildrenByName(tablesElement,
					"table");
			tableElements
					.forEach(tableElement -> createTable(tableElement, cpu));
		});
	}

	private void createTable(Element tableElement, Cpu cpu) {
		MemoryTable table = new MemoryTable();
		table.getName().set(tableElement.getAttribute("name"));
		cpu.getMemory().getMemoryTables().add(table);

		List<Element> variableElements = getChildrenByName(tableElement,
				"variable");
		variableElements.forEach(variableElement -> table.getVariables().add(
				new MemoryTableVariable(variableElement
						.getAttribute("variable"), variableElement
						.getAttribute("newValue"))));
	}

	private Program createProgram(Element cpuElement, Cpu cpu, String path)
			throws IOException, DOMException, URISyntaxException {
		List<Element> programElements = getChildrenByName(cpuElement, "program");
		if (programElements.size() != 1) {
			throw new PlcFactoryException(path,
					"Exactly one program node is required; Cpu: " + cpu);
		}

		Element programElement = programElements.get(0);

		long targetCycleTime = Integer.parseInt(programElement
				.getAttribute("cycleTime"));

		ScriptFile scriptFiles[] = getScriptFiles(programElement, path);

		return new ProgramImpl(cpu, new ScriptEngineManager(),
				new Precompiler(), targetCycleTime, scriptFiles);
	}

	private ScriptFile[] getScriptFiles(Element programElement, String path)
			throws DOMException, IOException, URISyntaxException {
		List<ScriptFile> result = new ArrayList<>();
		List<Element> sourceElements = getChildrenByName(programElement, "file");
		for (Element e : sourceElements) {
			URI file = new File(path).getParentFile().toURI()
					.resolve(e.getTextContent());
			byte[] bytes = Files.readAllBytes(Paths.get(file));
			result.add(new ScriptFile(e.getTextContent(), new String(bytes)));
		}

		return result.toArray(new ScriptFile[result.size()]);
	}

	private Memory createMemory(Element cpuElement) {
		Map<String, MemoryArea> areas = new HashMap<>();

		// create memory areas configured in configuration file
		List<Element> memoryElements = getChildrenByName(cpuElement, "memory");
		memoryElements.forEach((memoryElement) -> {
			List<Element> areaElements = getChildrenByName(memoryElement,
					"area");

			areaElements.forEach((areaElement) -> {
				String name = areaElement.getAttribute("name");
				int size = Integer.parseInt(areaElement.getAttribute("size"));

				areas.put(name, new MemoryAreaImpl(name, size, false));
			});
		});

		// create default memory areas not redefined in configuration file
		DEFAULT_MEMORY_AREAS.entrySet().forEach(
				(entry) -> {
					if (!areas.containsKey(entry.getKey())) {
						areas.put(
								entry.getKey(),
								new MemoryAreaImpl(entry.getKey(), entry
										.getValue(), true));
					}
				});

		return new MemoryImpl(new AddressParserFactory(),
				new DataTypeFactory(), areas.values().toArray(
						new MemoryArea[areas.size()]));
	}

	private List<Element> getChildrenByName(Element cpuElement, String name) {
		// looks like Element.getElementsByName operates within the whole
		// document context instead of within the element it is invoked on as
		// specified

		List<Element> result = new ArrayList<>();
		for (int i = 0; i < cpuElement.getChildNodes().getLength(); i++) {
			Node node = cpuElement.getChildNodes().item(i);
			if ((node instanceof Element)
					&& node.getNodeName().equalsIgnoreCase(name)) {
				result.add((Element) node);
			}
		}

		return result;
	}

	/**
	 *
	 * @return new empty PLC instance
	 */
	public Plc createNew() {
		return new PlcImpl(new PutGetServerImpl(PORT));
	}
}
