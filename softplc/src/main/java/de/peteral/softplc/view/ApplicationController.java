package de.peteral.softplc.view;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import de.peteral.softplc.SoftplcApplication;
import de.peteral.softplc.comm.tasks.CommunicationTaskFactory;

/**
 * Root panel Java FX controller.
 *
 * @author peteral
 *
 */
public class ApplicationController {

	private SoftplcApplication application;

	/**
	 * Initializes the controller with main app reference.
	 *
	 * @param application
	 */
	public void setApplication(SoftplcApplication application) {
		this.application = application;
	}

	/**
	 * Opens an about dialog.
	 */
	@FXML
	private void handleAbout() {
		// TODO use proper about dialog
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Softplc");
		alert.setHeaderText("About");
		StringBuilder builder = new StringBuilder(
				"Author: Ladislav Petera, 2014-2015\n");
		new CommunicationTaskFactory().logContents(builder);
		alert.setContentText(builder.toString());

		alert.showAndWait();
	}

	/**
	 * Opens a FileChooser to let the user select an address book to load.
	 */
	@FXML
	private void handleOpen() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show open file dialog
		File file = fileChooser.showOpenDialog(application.getPrimaryStage());

		if (file != null) {
			application.loadPlcFromFile(file);
		}
	}

	@FXML
	private void handleSave() {
		File currentFile = application.getLastOpenedFilePath();
		if (currentFile != null) {
			application.save(currentFile);
		} else {
			handleSaveAs();
		}
	}

	@FXML
	private void handleSaveAs() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(application.getPrimaryStage());

		if (file != null) {
			// Make sure it has the correct extension
			if (!file.getPath().endsWith(".xml")) {
				file = new File(file.getPath() + ".xml");
			}
			application.save(file);
		}
	}

	@FXML
	private void handleNew() {
		application.newPlc();
	}

	@FXML
	private void handleExit() {
		System.exit(0);
	}
}
