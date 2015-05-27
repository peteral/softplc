package de.peteral.softplc.view;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import de.peteral.softplc.SoftplcApplication;
import de.peteral.softplc.memorytables.MemoryTable;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.MemoryArea;
import de.peteral.softplc.model.Plc;
import de.peteral.softplc.program.ScriptFile;

/**
 * Actual PLC view controller.
 *
 * @author peteral
 *
 */
public class ActualViewController {

	@FXML
	private TableView<Cpu> cpuTable;
	@FXML
	private TableColumn<Cpu, Number> cpuSlotColumn;
	@FXML
	private TableColumn<Cpu, String> cpuModeColumn;
	@FXML
	private TableColumn<Cpu, Number> cpuCycleActColumn;
	@FXML
	private TableColumn<Cpu, Number> cpuCycleTarColumn;
	@FXML
	private TableColumn<Cpu, Number> cpuConnectionsColumn;

	@FXML
	private TableView<MemoryArea> memoryTable;
	@FXML
	private TableColumn<MemoryArea, String> memoryAreaColumn;
	@FXML
	private TableColumn<MemoryArea, Number> memorySizeColumn;

	@FXML
	private TableView<ScriptFile> programTable;
	@FXML
	private TableColumn<ScriptFile, String> programNameColumn;

	@FXML
	private TableView<MemoryTable> memoryTableTable;
	@FXML
	private TableColumn<MemoryTable, String> memoryTableNameColumn;

	private SoftplcApplication mainApp;
	private Plc plc;
	private Cpu currentCpu;
	private MemoryTable currentMemoryTable;

	/**
	 * Initializes the controller with main application reference.
	 *
	 * @param mainApp
	 */
	public void setMainApp(SoftplcApplication mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		cpuSlotColumn.setCellValueFactory(cellData -> cellData.getValue()
				.getSlot());
		cpuModeColumn.setCellValueFactory(cellData -> cellData.getValue()
				.getStatusProperty());
		cpuCycleTarColumn.setCellValueFactory(cellData -> cellData.getValue()
				.getProgram().getTargetCycleTime());
		cpuCycleActColumn.setCellValueFactory(cellData -> cellData.getValue()
				.getProgram().getCurrentCycleTime());

		memoryAreaColumn.setCellValueFactory(cellData -> cellData.getValue()
				.getAreaCode());
		memorySizeColumn.setCellValueFactory(cellData -> cellData.getValue()
				.getSize());

		memoryTableNameColumn.setCellValueFactory(cellData -> cellData
				.getValue().getName());

		programNameColumn.setCellValueFactory(data -> data.getValue()
				.getFileName());

		showCpuDetails(null);

		cpuTable.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(observable, oldValue, newValue) -> showCpuDetails(newValue));

		memoryTableTable
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(observable, oldValue, newValue) -> showMemoryTable(newValue));
	}

	private void showCpuDetails(Cpu newValue) {
		currentCpu = newValue;

		if (newValue == null) {
			memoryTable.setItems(null);
			programTable.setItems(null);
			memoryTableTable.setItems(null);
		} else {
			memoryTable.setItems(newValue.getMemory().getMemoryAreaList());
			programTable.setItems(newValue.getProgram().getScriptFiles());
			memoryTableTable.setItems(newValue.getMemory().getMemoryTables());
		}

	}

	private void showMemoryTable(MemoryTable newValue) {
		currentMemoryTable = newValue;

		if (newValue == null) {
		} else {

		}

	}

	/**
	 * Sets the PLC to be observed by the ui.
	 *
	 * @param plc
	 */
	public void setPlc(Plc plc) {
		this.plc = plc;

		cpuTable.setItems(plc.getCpus());
	}

	/**
	 *
	 * @return list of all CPUs selected by user
	 */
	public ObservableList<Cpu> getSelectedCpus() {
		return cpuTable.getSelectionModel().getSelectedItems();
	}

	@FXML
	private void handleAddMemoryTable() {
		currentCpu.getMemory().getMemoryTables().add(new MemoryTable());
	}

	@FXML
	private void handleDeleteMemoryTable() {

	}

	@FXML
	private void handleStart() {
		getSelectedCpus().forEach(cpu -> cpu.start());
	}

	@FXML
	private void handleStop() {
		getSelectedCpus().forEach(cpu -> cpu.stop());
	}

	@FXML
	private void handleStartAll() {
		mainApp.getPlc().getCpus().forEach(cpu -> cpu.start());
	}

	@FXML
	private void handleStopAll() {
		mainApp.getPlc().getCpus().forEach(cpu -> cpu.stop());
	}
}
