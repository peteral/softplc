package de.peteral.softplc.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.NumberStringConverter;
import de.peteral.softplc.cpu.ErrorLogEntry;
import de.peteral.softplc.memorytables.MemoryTable;
import de.peteral.softplc.memorytables.MemoryTableUpdateTask;
import de.peteral.softplc.memorytables.MemoryTableVariable;
import de.peteral.softplc.memorytables.MemoryTableWriteTask;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.CpuStatus;
import de.peteral.softplc.model.MemoryArea;
import de.peteral.softplc.plc.PlcFactory;
import de.peteral.softplc.program.ScriptFile;

/**
 * Controller for CPU status display.
 *
 * @author peteral
 *
 */
public class CpuViewController {
	private static final long OBSERVE_PERIOD = 100L;
	private static final long FORCE_PERIOD = 100L;
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
	@FXML
	private CheckMenuItem observeMemoryTableItem;
	@FXML
	private CheckMenuItem forceMemoryTableItem;

	@FXML
	private TableView<ErrorLogEntry> errorLogTable;
	@FXML
	private TableColumn<ErrorLogEntry, LocalDate> errorLogTimestampColumn;
	@FXML
	private TableColumn<ErrorLogEntry, String> errorLogLevelColumn;
	@FXML
	private TableColumn<ErrorLogEntry, String> errorLogModuleColumn;
	@FXML
	private TableColumn<ErrorLogEntry, String> errorLogMessageColumn;

	private Cpu currentCpu;
	private MemoryTable currentMemoryTable;
	private TimerTask updateMemoryTableTask;
	private Timer timer;
	private TimerTask forceMemoryTableTask;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		memoryAreaColumn.setCellValueFactory(cellData -> cellData.getValue()
				.getAreaCode());
		memoryAreaColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		memorySizeColumn.setCellValueFactory(cellData -> cellData.getValue()
				.getSize());
		memorySizeColumn.setCellFactory(TextFieldTableCell
				.forTableColumn(new NumberStringConverter()));

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

		errorLogTimestampColumn.setCellValueFactory(data -> data.getValue()
				.getTimestamp());
		errorLogLevelColumn.setCellValueFactory(data -> data.getValue()
				.getLevel());
		errorLogModuleColumn.setCellValueFactory(data -> data.getValue()
				.getModule());
		errorLogMessageColumn.setCellValueFactory(data -> data.getValue()
				.getMessage());

		timer = new Timer();

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
			errorLogTable.setItems(null);
		} else {
			memoryTable.setItems(newValue.getMemory().getMemoryAreas());
			programTable.setItems(newValue.getProgram().getScriptFiles());
			memoryTableTable.setItems(newValue.getMemory().getMemoryTables());
			errorLogTable.setItems(newValue.getErrorLog().getEntries());
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

	@FXML
	private void handleDeleteMemoryTableVariable() {
	}

	@FXML
	private void handleReadMemoryTable() {
		currentCpu.addCommunicationTask(new MemoryTableUpdateTask(
				currentMemoryTable));
	}

	@FXML
	private void handleWriteMemoryTable() {
		List<MemoryTableVariable> variables = new ArrayList<>();

		currentMemoryTable.getVariables().forEach(variable -> {
			String value = variable.getNewValue().get().trim();

			if (!value.isEmpty() && !value.startsWith("//")) {
				variables.add(variable);
			}
		});

		if (!variables.isEmpty()) {
			currentCpu.addCommunicationTask(new MemoryTableWriteTask(variables
					.toArray(new MemoryTableVariable[variables.size()])));
		}
	}

	@FXML
	private void handleWriteMemoryTableVariable() {
		MemoryTableVariable variable = memoryTableVariableTable
				.getSelectionModel().getSelectedItem();

		if (variable == null) {
			return;
		}

		String value = variable.getNewValue().get().trim();

		if (!value.isEmpty() && !value.startsWith("//")) {
			currentCpu.addCommunicationTask(new MemoryTableWriteTask(variable));
		}
	}

	@FXML
	private void handleObserveMemoryTable() {
		if (observeMemoryTableItem.isSelected()) {
			// start background task
			updateMemoryTableTask = new TimerTask() {
				@Override
				public void run() {
					if ((currentCpu.getStatus() == CpuStatus.RUN)
							&& (currentMemoryTable != null)) {
						handleReadMemoryTable();
					}
				}
			};

			timer.scheduleAtFixedRate(updateMemoryTableTask, 0, OBSERVE_PERIOD);
		} else {
			// stop background task
			updateMemoryTableTask.cancel();
			timer.purge();
		}
	}

	@FXML
	private void handleForceMemoryTable() {
		if (forceMemoryTableItem.isSelected()) {
			// start background task
			forceMemoryTableTask = new TimerTask() {
				@Override
				public void run() {
					if ((currentCpu.getStatus() == CpuStatus.RUN)
							&& (currentMemoryTable != null)) {
						handleWriteMemoryTable();
					}
				}
			};

			timer.scheduleAtFixedRate(forceMemoryTableTask, 0, FORCE_PERIOD);
		} else {
			// stop background task
			forceMemoryTableTask.cancel();
			timer.purge();
		}
	}

	@FXML
	private void handleAddMemoryArea() {
		currentCpu.getMemory().addMemoryArea(
				new PlcFactory().createMemoryArea());
	}

	@FXML
	private void handleDeleteMemoryArea() {
		currentCpu.getMemory().removeMemoryAreas(
				memoryTable.getSelectionModel().getSelectedItems());
	}

	@FXML
	private void handleSaveSnapshot() {
	}

	@FXML
	private void handleLoadSnapshot() {
	}

	@FXML
	private void handleAddSourceFile() {
	}

	@FXML
	private void handleDeleteSourceFile() {
	}

	@FXML
	private void handleToggleCommentVariable() {
		MemoryTableVariable variable = memoryTableVariableTable
				.getSelectionModel().getSelectedItem();

		if (variable == null) {
			return;
		}

		String value = variable.getNewValue().get().trim();

		if (value.isEmpty()) {
			return;
		}

		if (value.startsWith("//")) {
			variable.getNewValue().set(value.substring(2));
		} else {
			variable.getNewValue().set("//" + value);
		}
	}

}
