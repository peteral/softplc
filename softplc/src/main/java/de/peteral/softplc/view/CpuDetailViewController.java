package de.peteral.softplc.view;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import de.peteral.softplc.factory.PlcFactory;
import de.peteral.softplc.file.FileManager;
import de.peteral.softplc.file.FileUtil;
import de.peteral.softplc.memorytables.MemoryTableUpdateTask;
import de.peteral.softplc.memorytables.MemoryTableWriteTask;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.CpuStatus;
import de.peteral.softplc.model.ErrorLogEntry;
import de.peteral.softplc.model.MemoryArea;
import de.peteral.softplc.model.MemorySnapshot;
import de.peteral.softplc.model.MemoryTable;
import de.peteral.softplc.model.MemoryTableVariable;
import de.peteral.softplc.model.ScriptFile;
import de.peteral.softplc.serializer.MemorySerializer;
import de.peteral.softplc.view.error.ErrorDialog;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

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
	private TableColumn<MemoryTableVariable, String> memoryTableVariableCommentColumn;
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

	@FXML
	private TableView<MemorySnapshot> snapshotTable;
	@FXML
	private TableColumn<MemorySnapshot, Boolean> snapshotDefaultColumn;
	@FXML
	private TableColumn<MemorySnapshot, String> snapshotFileColumn;

	private Cpu currentCpu;
	private MemoryTable currentMemoryTable;
	private TimerTask updateMemoryTableTask;
	private Timer timer;
	private TimerTask forceMemoryTableTask;
	private Stage primaryStage;
	private FileManager fileManager;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	void initialize() {
		memoryAreaColumn.setCellValueFactory(cellData -> cellData.getValue().getAreaCode());
		memoryAreaColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		memorySizeColumn.setCellValueFactory(cellData -> cellData.getValue().getSize());
		memorySizeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));

		memoryTableNameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());
		memoryTableNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		memoryTableVariableNameColumn.setCellValueFactory(cellData -> cellData.getValue().getVariable());
		memoryTableVariableNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		memoryTableVariableCurrentColumn.setCellValueFactory(cellData -> cellData.getValue().getCurrentValue());
		memoryTableVariableNewColumn.setCellValueFactory(cellData -> cellData.getValue().getNewValue());
		memoryTableVariableNewColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		memoryTableVariableCommentColumn.setCellValueFactory(cellData -> cellData.getValue().getComment());
		memoryTableVariableCommentColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		programNameColumn.setCellValueFactory(data -> data.getValue().getFileName());

		errorLogTimestampColumn.setCellValueFactory(data -> data.getValue().getTimestamp());
		errorLogLevelColumn.setCellValueFactory(data -> data.getValue().getLevel());
		errorLogModuleColumn.setCellValueFactory(data -> data.getValue().getModule());
		errorLogMessageColumn.setCellValueFactory(data -> data.getValue().getMessage());

		snapshotDefaultColumn.setCellValueFactory(cellData -> cellData.getValue().isDefault());
		snapshotDefaultColumn.setCellFactory(CheckBoxTableCell.forTableColumn(snapshotDefaultColumn));
		snapshotFileColumn.setCellValueFactory(cellData -> cellData.getValue().getFileName());

		timer = new Timer();

		update(null);

		memoryTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		memoryTableTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		programTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		memoryTableVariableTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		snapshotTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		memoryTableTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showMemoryTable(newValue));
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
			snapshotTable.setItems(null);
		} else {
			memoryTable.setItems(newValue.getMemory().getMemoryAreas());
			programTable.setItems(newValue.getProgram().getScriptFiles());
			memoryTableTable.setItems(newValue.getMemory().getMemoryTables());
			errorLogTable.setItems(newValue.getErrorLog().getEntries());
			snapshotTable.setItems(newValue.getSnapshots());
		}

	}

	private void showMemoryTable(MemoryTable newValue) {
		currentMemoryTable = newValue;

		if (newValue == null) {
			memoryTableVariableTable.setItems(null);
		} else {
			memoryTableVariableTable.setItems(currentMemoryTable.getVariables());
		}

	}

	@FXML
	void handleAddMemoryTable() {
		currentCpu.getMemory().getMemoryTables().add(new MemoryTable());
	}

	@FXML
	void handleDeleteMemoryTable() {
		currentCpu.getMemory().getMemoryTables().removeAll(memoryTableTable.getSelectionModel().getSelectedItems());
	}

	@FXML
	void handleAddMemoryTableVariable() {
		currentMemoryTable.getVariables().add(new MemoryTableVariable());
	}

	@FXML
	void handleDeleteMemoryTableVariable() {
		currentMemoryTable.getVariables().removeAll(memoryTableVariableTable.getSelectionModel().getSelectedItems());
	}

	@FXML
	void handleReadMemoryTable() {
		currentCpu.addCommunicationTask(new MemoryTableUpdateTask(currentMemoryTable));
	}

	@FXML
	void handleWriteMemoryTable() {
		writeVariables(currentMemoryTable.getVariables());
	}

	private void writeVariables(ObservableList<MemoryTableVariable> vars) {
		writeVariables(vars, null);
	}

	private void writeVariables(ObservableList<MemoryTableVariable> vars, String val) {
		List<MemoryTableVariable> variables = new ArrayList<>();

		vars.forEach(variable -> {
			if (val == null) {
				String value = variable.getNewValue().get();
				value = (value == null) ? "" : value.trim();

				if (!value.isEmpty() && !value.startsWith("//")) {
					variables.add(variable);
				}
			} else {
				variables.add(new MemoryTableVariable(variable.getVariable().get(), val));
			}
		});

		if (!variables.isEmpty()) {
			currentCpu.addCommunicationTask(
					new MemoryTableWriteTask(variables.toArray(new MemoryTableVariable[variables.size()])));
		}
	}

	@FXML
	void handleWriteMemoryTableVariable() {
		writeVariables(memoryTableVariableTable.getSelectionModel().getSelectedItems());
	}

	@FXML
	void handleObserveMemoryTable() {
		if (observeMemoryTableItem.isSelected()) {
			// start background task
			updateMemoryTableTask = new TimerTask() {
				@Override
				public void run() {
					if ((currentCpu.getStatus() == CpuStatus.RUN) && (currentMemoryTable != null)) {
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
	void handleForceMemoryTable() {
		if (forceMemoryTableItem.isSelected()) {
			// start background task
			forceMemoryTableTask = new TimerTask() {
				@Override
				public void run() {
					if ((currentCpu.getStatus() == CpuStatus.RUN) && (currentMemoryTable != null)) {
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
	void handleAddMemoryArea() {
		currentCpu.getMemory().addMemoryArea(new PlcFactory().createMemoryArea());
	}

	@FXML
	void handleDeleteMemoryArea() {
		currentCpu.getMemory().removeMemoryAreas(memoryTable.getSelectionModel().getSelectedItems());
	}

	@FXML
	void handleAddSourceFile() {
		FileChooser fileChooser = createFileChooser(new FileChooser.ExtensionFilter("JavaScript files (*.js)", "*.js"),
				null);

		// Show open file dialog
		List<File> files = fileChooser.showOpenMultipleDialog(primaryStage);
		if (files == null) {
			return;
		}

		files.forEach(file -> {
			try {
				ScriptFile scriptFile = new ScriptFile(file.getAbsolutePath(), file);
				scriptFile.reload();

				currentCpu.getProgram().getScriptFiles().add(scriptFile);
			} catch (IOException e) {
				ErrorDialog.show("Failed adding file [" + file.getPath() + "]", e);
			}
		});
	}

	@FXML
	void handleDeleteSourceFile() {
		currentCpu.getProgram().getScriptFiles().removeAll(programTable.getSelectionModel().getSelectedItems());
	}

	@FXML
	void handleToggleCommentVariable() {
		memoryTableVariableTable.getSelectionModel().getSelectedItems().forEach(variable -> {

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
		});
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
	void handleToAbsolutePath() {
		programTable.getSelectionModel().getSelectedItems().forEach(scriptFile -> {
			String absolute = FileUtil.toAbsolute(scriptFile.getFileName().get(), getBaseFile());
			if (absolute != null) {
				scriptFile.getFileName().set(absolute);
			}
		});
	}

	private File getBaseFile() {
		return (currentCpu.getPlc().getPath() == null) ? null : currentCpu.getPlc().getPath().getParentFile();
	}

	@FXML
	void handleToRelativePath() {
		programTable.getSelectionModel().getSelectedItems().forEach(scriptFile -> {
			if (currentCpu.getPlc().getPath() == null) {
				return;
			}
			String relative = FileUtil.toRelative(scriptFile.getFileName().get(), getBaseFile());
			scriptFile.getFileName().set(relative);
		});
	}

	@FXML
	void handleSourceUp() {
		int selected = programTable.getSelectionModel().getSelectedIndex();
		if (selected > 0) {
			Collections.swap(currentCpu.getProgram().getScriptFiles(), selected, selected - 1);
		}
	}

	@FXML
	void handleSourceDown() {
		int selected = programTable.getSelectionModel().getSelectedIndex();
		if (selected < (currentCpu.getProgram().getScriptFiles().size() - 1)) {
			Collections.swap(currentCpu.getProgram().getScriptFiles(), selected, selected + 1);
		}
	}

	@FXML
	void handleLoadProgram() {
		currentCpu.getProgram().reloadFromDisk();
		currentCpu.loadProgram(currentCpu.getProgram());
	}

	@FXML
	void handleAddMemoryAreaRange() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(CpuDetailViewController.class.getResource("AddMemoryAreaRangeDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add memory area range");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			AddMemoryAreaRangeDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			if (controller.isOkClicked()) {
				controller.createMemoryAreas().forEach(area -> currentCpu.getMemory().addMemoryArea(area));
			}
		} catch (IOException e) {
			ErrorDialog.show("Failed loading dialog", e);
		}
	}

	@FXML
	void handleCreateMain() {
		FileChooser fileChooser = createFileChooser(new FileChooser.ExtensionFilter("JavaScript files (*.js)", "*.js"),
				"main.js");

		// Show open file dialog
		File file = fileChooser.showSaveDialog(primaryStage);
		if (file == null) {
			return;
		}

		try {

			deployResource("/script/main.js", file);

			ScriptFile scriptFile = new ScriptFile(file.getAbsolutePath(), file);
			scriptFile.reload();

			currentCpu.getProgram().getScriptFiles().add(scriptFile);
		} catch (IOException e) {
			ErrorDialog.show("Failed creating main file", e);
		}
	}

	private FileChooser createFileChooser(FileChooser.ExtensionFilter extFilter, String initialFileName) {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		fileChooser.getExtensionFilters().add(extFilter);
		fileManager.setLastFolder(fileChooser);
		if (initialFileName != null) {
			fileChooser.setInitialFileName(initialFileName);
		}
		return fileChooser;
	}

	private void deployResource(String resourceName, File file) throws IOException {

		if (file.exists()) {
			file.delete();
		}

		try (InputStream inputStream = CpuDetailViewController.class.getResourceAsStream(resourceName)) {

			try (OutputStream outputStream = Files.newOutputStream(Paths.get(file.toURI()),
					StandardOpenOption.CREATE_NEW)) {

				byte[] buffer = new byte[inputStream.available()];
				inputStream.read(buffer);
				outputStream.write(buffer);
			}
		}
	}

	/**
	 * Initializes the controller with file manager reference.
	 *
	 * @param fileManager
	 */
	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	@FXML
	void handleWrite1() {
		writeVariables(memoryTableVariableTable.getSelectionModel().getSelectedItems(), "1");
	}

	@FXML
	void handleWrite0() {
		writeVariables(memoryTableVariableTable.getSelectionModel().getSelectedItems(), "0");
	}

	@FXML
	void handleNewSnapshot() {
		FileChooser fileChooser = createFileChooser(
				new FileChooser.ExtensionFilter("Memory snapshot files (*.snapshot)", "*.snapshot"),
				currentCpu.getName().get() + "_"
						+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuuMMddHHmmss")) + ".snapshot");

		File file = fileChooser.showSaveDialog(primaryStage);
		if (file == null) {
			return;
		}

		try {
			MemorySnapshot snapshot = new MemorySnapshot(false, file.getCanonicalPath());

			MemorySerializer.save(snapshot.getFile(getBaseFile()), currentCpu.getMemory());
			currentCpu.getSnapshots().add(snapshot);

		} catch (Exception e) {
			ErrorDialog.show("Failed creating snapshot file", e);
		}
	}

	@FXML
	void handleOverwriteSnapshot() {
		MemorySnapshot snapshot = snapshotTable.getSelectionModel().getSelectedItem();
		if (snapshot == null) {
			return;
		}

		MemorySerializer.save(snapshot.getFile(getBaseFile()), currentCpu.getMemory());
	}

	@FXML
	void handleImportSnapshot() {
		FileChooser fileChooser = createFileChooser(
				new FileChooser.ExtensionFilter("Memory snapshot files (*.snapshot)", "*.snapshot"),
				currentCpu.getName().get() + "_"
						+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuuMMddHHmmss")) + ".snapshot");

		List<File> files = fileChooser.showOpenMultipleDialog(primaryStage);
		if (files == null) {
			return;
		}

		files.forEach(file -> {
			try {
				MemorySnapshot snapshot = new MemorySnapshot(false, file.getCanonicalPath());
				currentCpu.getSnapshots().add(snapshot);

			} catch (Exception e) {
				ErrorDialog.show("Failed importing snapshot file", e);
			}
		});
	}

	@FXML
	void handleDeleteSnapshot() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete from snapshot");
		alert.setHeaderText("Remove snapshot(s) from project only or delete on disk?");
		alert.setContentText(
				"Choose your option:\n - Remove = only remove from project\n - Delete = delete from disk as well");

		ButtonType buttonTypeRemove = new ButtonType("Remove");
		ButtonType buttonTypeDelete = new ButtonType("Delete");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeRemove, buttonTypeDelete, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeCancel) {
			return;
		}

		if (result.get() == buttonTypeDelete) {
			snapshotTable.getSelectionModel().getSelectedItems().forEach(snapshot -> {
				File file = snapshot.getFile(getBaseFile());
				try {
					file.delete();
				} catch (Exception e) {
					ErrorDialog.show("Failed deleting file [" + file + "]", e);
				}
			});
		}

		currentCpu.getSnapshots().removeAll(snapshotTable.getSelectionModel().getSelectedItems());
	}

	@FXML
	void handleSnapshotToAbsolute() {
		snapshotTable.getSelectionModel().getSelectedItems().forEach(snapshot -> {
			String absolute = FileUtil.toAbsolute(snapshot.getFileName().get(), getBaseFile());
			if (absolute != null) {
				snapshot.getFileName().set(absolute);
			}
		});
	}

	@FXML
	void handleSnapshotToRelative() {
		snapshotTable.getSelectionModel().getSelectedItems().forEach(snapshot -> {
			String relative = FileUtil.toRelative(snapshot.getFileName().get(), getBaseFile());
			if (relative != null) {
				snapshot.getFileName().set(relative);
			}
		});
	}

	@FXML
	void handleLoadSnapshot() {
		MemorySnapshot snapshot = snapshotTable.getSelectionModel().getSelectedItem();
		if (snapshot == null) {
			return;
		}

		MemorySerializer.load(snapshot.getFile(getBaseFile()), currentCpu.getMemory());
	}

	@FXML
	void handleResetMemory() {
		currentCpu.getMemory().reset();
	}
}
