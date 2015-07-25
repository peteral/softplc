package de.peteral.softplc.view;

import java.io.File;

import de.peteral.softplc.comm.tasks.CommunicationTaskFactory;
import de.peteral.softplc.file.FileManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Root panel Java FX controller.
 *
 * @author peteral
 *
 */
public class ApplicationController {

	private FileManager fileManager;
	private Stage stage;

	/**
	 * Initializes the controller with file manager reference.
	 *
	 * @param fileManager
	 */
	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	/**
	 * Assigns current stage
	 *
	 * @param stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;

	}

	/**
	 * Opens an about dialog.
	 */
	@FXML
	void handleAbout() {
		// TODO use proper about dialog
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Softplc");
		alert.setHeaderText("About");
		StringBuilder builder = new StringBuilder("Author: Ladislav Petera, 2014-2015\n");
		new CommunicationTaskFactory().logContents(builder);
		alert.setContentText(builder.toString());

		alert.showAndWait();
	}

	/**
	 * Opens a FileChooser to let the user select an address book to load.
	 */
	@FXML
	void handleOpen() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		fileManager.setLastFolder(fileChooser);

		// Show open file dialog
		File file = fileChooser.showOpenDialog(stage);

		if (file != null) {
			fileManager.loadPlcFromFile(file);
		}
	}

	@FXML
	void handleSave() {
		File currentFile = fileManager.getLastOpenedFilePath();
		if (currentFile != null) {
			fileManager.save(currentFile);
		} else {
			handleSaveAs();
		}
	}

	@FXML
	void handleSaveAs() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		fileManager.setLastFolder(fileChooser);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(stage);

		if (file != null) {
			// Make sure it has the correct extension
			if (!file.getPath().endsWith(".xml")) {
				file = new File(file.getPath() + ".xml");
			}
			fileManager.save(file);
		}
	}

	@FXML
	void handleNew() {
		fileManager.newPlc();
	}

	@FXML
	void handleExit() {
		System.exit(0);
	}
}
