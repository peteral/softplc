package de.peteral.softplc.view;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import de.peteral.softplc.memorytables.MemoryTable;
import de.peteral.softplc.memorytables.MemoryTableVariable;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.MemoryArea;
import de.peteral.softplc.program.ScriptFile;

/**
 * Controller for CPU status display.
 *
 * @author peteral
 *
 */
public class CpuViewController {
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

	@FXML
	private TableView<MemoryTableVariable> memoryTableVariableTable;
	@FXML
	private TableColumn<MemoryTableVariable, String> memoryTableVariableNameColumn;
	@FXML
	private TableColumn<MemoryTableVariable, String> memoryTableVariableCurrentColumn;
	@FXML
	private TableColumn<MemoryTableVariable, String> memoryTableVariableNewColumn;

	private Cpu currentCpu;
	private MemoryTable currentMemoryTable;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		memoryAreaColumn.setCellValueFactory(cellData -> cellData.getValue()
				.getAreaCode());
		memorySizeColumn.setCellValueFactory(cellData -> cellData.getValue()
				.getSize());

		memoryTableNameColumn.setCellValueFactory(cellData -> cellData
				.getValue().getName());
		memoryTableNameColumn.setCellFactory(TextFieldTableCell
				.forTableColumn());

		memoryTableVariableNameColumn.setCellValueFactory(cellData -> cellData
				.getValue().getVariable());
		memoryTableVariableNameColumn.setCellFactory(TextFieldTableCell
				.forTableColumn());
		memoryTableVariableCurrentColumn
				.setCellValueFactory(cellData -> cellData.getValue()
						.getCurrentValue());
		memoryTableVariableNewColumn.setCellValueFactory(cellData -> cellData
				.getValue().getNewValue());
		memoryTableVariableNewColumn.setCellFactory(TextFieldTableCell
				.forTableColumn());

		programNameColumn.setCellValueFactory(data -> data.getValue()
				.getFileName());

		update(null);

		memoryTableTable
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(observable, oldValue, newValue) -> showMemoryTable(newValue));
	}

	/**
	 * Updates status from cpu instance
	 *
	 * @param newValue
	 *            new cpu to display
	 */
	public void update(Cpu newValue) {
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
			memoryTableVariableTable.setItems(null);
		} else {
			memoryTableVariableTable
					.setItems(currentMemoryTable.getVariables());
		}

	}

	@FXML
	private void handleAddMemoryTable() {
		currentCpu.getMemory().getMemoryTables().add(new MemoryTable());
	}

	@FXML
	private void handleDeleteMemoryTable() {

	}

	@FXML
	private void handleAddMemoryTableVariable() {
		currentMemoryTable.getVariables().add(new MemoryTableVariable());
	}

}
