package de.peteral.softplc.ui.view;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import de.peteral.softplc.ui.SoftplcApplication;

/**
 * Root panel Java FX controller.
 *
 * @author peteral
 *
 */
public class RootPanelController {

	private SoftplcApplication mainApp;

	/**
	 * Initializes the controller with main app reference.
	 *
	 * @param mainApp
	 */
	public void setMainApp(SoftplcApplication mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * Opens an about dialog.
	 */
	@FXML
	private void handleAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Softplc");
		alert.setHeaderText("About");
		alert.setContentText("Author: Ladislav Petera, 2014-2015");

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
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

		if (file != null) {
			mainApp.loadPlcFromFile(file);
		}
	}

	@FXML
	private void handleStart() {

	}

	@FXML
	private void handleStop() {

	}

	@FXML
	private void handleStartAll() {

	}

	@FXML
	private void handleStopAll() {

	}

	@FXML
	private void handleSave() {

	}

	@FXML
	private void handleSaveAs() {

	}

	@FXML
	private void handleNew() {

	}

	@FXML
	private void handleExit() {
		System.exit(0);
	}
}
