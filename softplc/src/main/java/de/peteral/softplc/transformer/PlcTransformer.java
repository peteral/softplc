package de.peteral.softplc.transformer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.MemoryArea;
import de.peteral.softplc.model.MemorySnapshot;
import de.peteral.softplc.model.MemoryTable;
import de.peteral.softplc.model.Plc;
import de.peteral.softplc.model.ScriptFile;
import de.peteral.softplc.model.Symbol;

/**
 * Transforms current model to DOM document.
 *
 * @author peteral
 */
public class PlcTransformer {

	/**
	 * Transforms a plc instance to DOM document
	 *
	 * @param plc
	 * @return DOM document containing PLC configuration.
	 * @throws ParserConfigurationException
	 */
	public Document transform(Plc plc) throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("softplc");
		doc.appendChild(rootElement);

		plc.getCpus().forEach(cpu -> appendCpu(doc, cpu));

		return doc;
	}

	private void appendCpu(Document doc, Cpu cpu) {
		Element cpuElement = doc.createElement("cpu");
		cpuElement.setAttribute("slot", "" + cpu.getSlot().get());
		cpuElement.setAttribute("name", cpu.getName().get());
		cpuElement.setAttribute("connections", "" + cpu.getMaxConnections().get());
		cpuElement.setAttribute("maxBlockSize", "" + cpu.getMaxDataSize().get());
		cpuElement.setAttribute("status", "" + cpu.getStatus().toString());
		doc.getDocumentElement().appendChild(cpuElement);

		Element memoryElement = doc.createElement("memory");
		cpuElement.appendChild(memoryElement);
		cpu.getMemory().getMemoryAreas().forEach(memoryArea -> appendMemoryArea(memoryArea, memoryElement, doc));

		Element programElement = doc.createElement("program");
		cpuElement.appendChild(programElement);
		programElement.setAttribute("cycleTime", "" + cpu.getTargetCycleTime());
		cpu.getProgram().getScriptFiles().forEach(file -> addFile(file, programElement, doc));

		if (!cpu.getMemory().getMemoryTables().isEmpty()) {
			Element memoryTablesElement = doc.createElement("tables");
			cpuElement.appendChild(memoryTablesElement);

			cpu.getMemory().getMemoryTables().forEach(table -> addMemoryTable(memoryTablesElement, table, doc));
		}

		if (!cpu.getSnapshots().isEmpty()) {
			Element snapshotsElement = doc.createElement("snapshots");
			cpuElement.appendChild(snapshotsElement);

			cpu.getSnapshots().forEach(snapshot -> addSnapshot(snapshotsElement, snapshot, doc));
		}

		if (!cpu.getMemory().getSymbolTable().getAllSymbols().isEmpty()) {
			Element symbolsElement = doc.createElement("symbols");
			cpuElement.appendChild(symbolsElement);

			cpu.getMemory().getSymbolTable().getAllSymbols().forEach(symbol -> addSymbol(symbolsElement, symbol, doc));
		}
	}

	private void addSymbol(Element symbolsElement, Symbol symbol, Document doc) {
		Element symbolElement = doc.createElement("symbol");
		symbolElement.setAttribute("name", symbol.getName().get());
		symbolElement.setAttribute("address", symbol.getAddress().get());
		symbolsElement.appendChild(symbolElement);
	}

	private void addSnapshot(Element snapshotsElement, MemorySnapshot snapshot, Document doc) {

		Element snapshotElement = doc.createElement("snapshot");
		snapshotElement.setAttribute("default", "" + snapshot.isDefault().get());
		snapshotElement.setAttribute("file", snapshot.getFileName().get());
		snapshotsElement.appendChild(snapshotElement);
	}

	private void addMemoryTable(Element memoryTablesElement, MemoryTable table, Document doc) {

		Element tableElement = doc.createElement("table");
		tableElement.setAttribute("name", table.getName().get());
		memoryTablesElement.appendChild(tableElement);

		table.getVariables().forEach(variable -> {
			Element variableElement = doc.createElement("variable");
			variableElement.setAttribute("variable", variable.getVariable().get());
			String newValue = variable.getNewValue().get();
			variableElement.setAttribute("newValue", (newValue == null) ? "" : newValue);
			String comment = variable.getComment().get();
			variableElement.setAttribute("comment", (comment == null) ? "" : comment);
			tableElement.appendChild(variableElement);
		});
	}

	private void addFile(ScriptFile file, Element programElement, Document doc) {
		Element fileElement = doc.createElement("file");
		programElement.appendChild(fileElement);
		fileElement.setTextContent(file.getFileName().get());
	}

	private void appendMemoryArea(MemoryArea memoryArea, Element memoryElement, Document doc) {

		if (memoryArea.isDefaultArea()) {
			return;
		}

		Element memoryAreaElement = doc.createElement("area");
		memoryAreaElement.setAttribute("name", memoryArea.getAreaCode().get());
		memoryAreaElement.setAttribute("size", "" + memoryArea.getSize().get());
		memoryElement.appendChild(memoryAreaElement);
	}
}
