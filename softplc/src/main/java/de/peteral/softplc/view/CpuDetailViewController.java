package de.peteral.softplc.view;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
public class CpuDetailViewController {
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
	private Stage primaryStage;

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
		currentCpu.getMemory().getMemoryTables().remove(currentMemoryTable);
	}

	@FXML
	private void handleAddMemoryTableVariable() {
		currentMemoryTable.getVariables().add(new MemoryTableVariable());
	}

	@FXML
	private void handleDeleteMemoryTableVariable() {
		currentMemoryTable.getVariables()
				.removeAll(
						memoryTableVariableTable.getSelectionModel()
								.getSelectedItems());
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
		// TODO implement
	}

	@FXML
	private void handleLoadSnapshot() {
		// TODO implement
	}

	@FXML
	private void handleAddSourceFile() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"JavaScript files (*.js)", "*.js");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show open file dialog
		List<File> files = fileChooser.showOpenMultipleDialog(primaryStage);

		files.forEach(file -> {
			try {
				URI uri = file.toURI();
				byte[] bytes = Files.readAllBytes(Paths.get(uri));
				currentCpu
						.getProgram()
						.getScriptFiles()
						.add(new ScriptFile(file.getAbsolutePath(), new String(
								bytes)));
			} catch (IOException e) {
				// TODO error dialog
				e.printStackTrace();
			}
		});
	}

	@FXML
	private void handleDeleteSourceFile() {
		currentCpu.getProgram().getScriptFiles()
				.removeAll(programTable.getSelectionModel().getSelectedItems());
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

	/**
	 * assigns current stage
	 *
	 * @param primaryStage
	 */
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	@FXML
	private void handleToAbsolutePath() {
		programTable
				.getSelectionModel()
				.getSelectedItems()
				.forEach(
						scriptFile -> {
							File f = new File(scriptFile.getFileName().get());
							if (!f.isAbsolute()) {
								try {
									Path pathBase = Paths.get(currentCpu
											.getPlc().getPath().getParentFile()
											.getCanonicalPath());
									Path pathRelative = Paths.get(f.getPath());
									Path pathAbsolute = pathBase
											.resolve(pathRelative);
									scriptFile.getFileName().set(
											pathAbsolute.toString());
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
	}

	@FXML
	private void handleToRelativePath() {
		programTable
				.getSelectionModel()
				.getSelectedItems()
				.forEach(
						scriptFile -> {
							File f = new File(scriptFile.getFileName().get());
							if (f.isAbsolute()
									&& (currentCpu.getPlc().getPath() != null)) {
								try {
									Path pathAbsolute = Paths.get(f
											.getCanonicalPath());
									Path pathBase = Paths.get(currentCpu
											.getPlc().getPath().getParentFile()
											.getCanonicalPath());
									Path pathRelative = pathBase
											.relativize(pathAbsolute);

									scriptFile.getFileName().set(
											pathRelative.toString());
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
	}

	@FXML
	private void handleSourceUp() {
		int selected = programTable.getSelectionModel().getSelectedIndex();
		if (selected > 0) {
			Collections.swap(currentCpu.getProgram().getScriptFiles(),
					selected, selected - 1);
		}
	}

	@FXML
	private void handleSourceDown() {
		int selected = programTable.getSelectionModel().getSelectedIndex();
		if (selected < (currentCpu.getProgram().getScriptFiles().size() - 1)) {
			Collections.swap(currentCpu.getProgram().getScriptFiles(),
					selected, selected + 1);
		}
	}

	@FXML
	private void handleLoadProgram() {
		currentCpu.loadProgram(currentCpu.getProgram());
	}

	@FXML
	private void handleAddMemoryAreaRange() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(CpuDetailViewController.class
					.getResource("AddMemoryAreaRangeDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add memory area range");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			AddMemoryAreaRangeDialogController controller = loader
					.getController();
			controller.setDialogStage(dialogStage);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			if (controller.isOkClicked()) {
				controller.createMemoryAreas().forEach(
						area -> currentCpu.getMemory().addMemoryArea(area));
			}
		} catch (IOException e) {
			// TODO error handling
			e.printStackTrace();
		}
	}
}
