package de.peteral.softplc.view.error;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Dialog for displaying exceptions.
 *
 * @author peteral
 *
 */
public final class ErrorDialog {

	private ErrorDialog() {
	}

	/**
	 * Shows the error dialog
	 *
	 * @param message
	 *            additional message
	 * @param reason
	 *            error description
	 */
	public static void show(String message, Throwable reason) {
		Logger.getLogger("global").log(Level.WARNING, message, reason);

		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(message);
		alert.setContentText((reason == null) ? "" : reason.getMessage());

		alert.showAndWait();
	}
}
