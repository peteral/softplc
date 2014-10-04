package de.peteral.softplc.plc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.script.ScriptEngineManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.peteral.softplc.address.AddressParserFactory;
import de.peteral.softplc.cpu.CpuImpl;
import de.peteral.softplc.cpu.ErrorLogImpl;
import de.peteral.softplc.dataType.DataTypeFactory;
import de.peteral.softplc.memory.MemoryImpl;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.Memory;
import de.peteral.softplc.model.MemoryArea;
import de.peteral.softplc.model.Plc;
import de.peteral.softplc.model.Program;
import de.peteral.softplc.program.Precompiler;
import de.peteral.softplc.program.ProgramImpl;
import de.peteral.softplc.server.PutGetServerImpl;

/**
 * Creates a {@link Plc} instance from configuration file.
 *
 * @author peteral
 *
 */
public class PlcFactory {
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

			return createFromDocument(doc);
		} catch (IOException | ParserConfigurationException | SAXException e) {
			throw new PlcFactoryException(path, e);
		}
	}

	private Plc createFromDocument(Document doc) {
		List<Cpu> cpus = new ArrayList<>();

		NodeList cpuElements = doc.getElementsByTagName("cpu");
		for (int i = 0; i < cpuElements.getLength(); i++) {
			Element cpuElement = (Element) cpuElements.item(i);

			cpus.add(createCpu(cpuElement));
		}

		return new PlcImpl(new PutGetServerImpl(), cpus.toArray(new Cpu[cpus
				.size()]));
	}

	private Cpu createCpu(Element cpuElement) {
		int slot = Integer.parseInt(cpuElement.getAttribute("slot"));

		Memory memory = createMemory(cpuElement);

		Cpu cpu = new CpuImpl(slot, new ErrorLogImpl(),
				new ScheduledThreadPoolExecutor(1), memory);

		Program program = createProgram(cpuElement, cpu);

		cpu.loadProgram(program);

		return cpu;
	}

	private Program createProgram(Element cpuElement, Cpu cpu) {
		long targetCycleTime = 0;
		String sourceCode = "";
		// TODO Auto-generated method stub

		return new ProgramImpl(cpu, new ScriptEngineManager(),
				new Precompiler(), targetCycleTime, sourceCode);
	}

	private Memory createMemory(Element cpuElement) {
		List<MemoryArea> areas = new ArrayList<>();
		// TODO create default and configured memory areas

		return new MemoryImpl(new AddressParserFactory(),
				new DataTypeFactory(), areas.toArray(new MemoryArea[areas
						.size()]));
	}

}
