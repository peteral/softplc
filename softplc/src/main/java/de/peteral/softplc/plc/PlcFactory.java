package de.peteral.softplc.plc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.script.ScriptEngineManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.peteral.softplc.address.AddressParserFactory;
import de.peteral.softplc.comm.PutGetServerImpl;
import de.peteral.softplc.comm.RequestWorker;
import de.peteral.softplc.cpu.CpuImpl;
import de.peteral.softplc.cpu.ErrorLogImpl;
import de.peteral.softplc.datatype.DataTypeFactory;
import de.peteral.softplc.memory.MemoryAreaImpl;
import de.peteral.softplc.memory.MemoryImpl;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.Memory;
import de.peteral.softplc.model.MemoryArea;
import de.peteral.softplc.model.Plc;
import de.peteral.softplc.model.Program;
import de.peteral.softplc.program.Precompiler;
import de.peteral.softplc.program.ProgramImpl;

/**
 * Creates a {@link Plc} instance from configuration file.
 *
 * @author peteral
 *
 */
public class PlcFactory {
	private static final int PORT = 102;
	private static final Map<String, Integer> DEFAULT_MEMORY_AREAS = new HashMap<>();
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
		} catch (IOException | ParserConfigurationException | SAXException e) {
			throw new PlcFactoryException(path, e);
		}
	}

	private Plc createFromDocument(Document doc, String path)
			throws IOException {
		List<Cpu> cpus = new ArrayList<>();

		NodeList cpuElements = doc.getElementsByTagName("cpu");
		for (int i = 0; i < cpuElements.getLength(); i++) {
			Element cpuElement = (Element) cpuElements.item(i);

			cpus.add(createCpu(cpuElement, path));
		}

		return new PlcImpl(new PutGetServerImpl(PORT, new RequestWorker()),
				cpus.toArray(new Cpu[cpus.size()]));
	}

	private Cpu createCpu(Element cpuElement, String path) throws IOException {
		int slot = Integer.parseInt(cpuElement.getAttribute("slot"));

		Memory memory = createMemory(cpuElement);

		Cpu cpu = new CpuImpl(slot, new ErrorLogImpl(),
				new ScheduledThreadPoolExecutor(1), memory);

		Program program = createProgram(cpuElement, cpu, path);

		cpu.loadProgram(program);

		return cpu;
	}

	private Program createProgram(Element cpuElement, Cpu cpu, String path)
			throws IOException {
		List<Element> programElements = getChildrenByName(cpuElement, "program");
		if (programElements.size() != 1) {
			throw new PlcFactoryException(path,
					"Exactly one program node is required; Cpu: " + cpu);
		}

		Element programElement = programElements.get(0);

		long targetCycleTime = Integer.parseInt(programElement
				.getAttribute("cycleTime"));
		String sourceFile = programElement.getAttribute("file");

		byte[] bytes = Files.readAllBytes(Paths.get(sourceFile));

		return new ProgramImpl(cpu, new ScriptEngineManager(),
				new Precompiler(), targetCycleTime, new String(bytes));
	}

	private Memory createMemory(Element cpuElement) {
		Map<String, MemoryArea> areas = new HashMap<>();

		// create memory areas configured in configuration file
		List<Element> memoryElements = getChildrenByName(cpuElement, "memory");
		for (Element memoryElement : memoryElements) {
			List<Element> areaElements = getChildrenByName(memoryElement,
					"area");

			for (Element areaElement : areaElements) {
				String name = areaElement.getAttribute("name");
				int size = Integer.parseInt(areaElement.getAttribute("size"));

				areas.put(name, new MemoryAreaImpl(name, size));
			}
		}

		// create default memory areas not redefined in configuration file
		for (Entry<String, Integer> entry : DEFAULT_MEMORY_AREAS.entrySet()) {
			if (!areas.containsKey(entry.getKey())) {
				areas.put(entry.getKey(), new MemoryAreaImpl(entry.getKey(),
						entry.getValue()));
			}
		}

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
}
