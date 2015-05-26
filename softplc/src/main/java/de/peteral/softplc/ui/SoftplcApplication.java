package de.peteral.softplc.ui;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import de.peteral.softplc.ui.view.ActualViewController;
import de.peteral.softplc.ui.view.RootPanelController;

/**
 * Java FX application entry point.
 *
 * @author peteral
 *
 */
public class SoftplcApplication extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Softplc");

		// FIXME icon path
		this.primaryStage.getIcons().add(
				new Image("file:src/main/resources/images/softplc_32.png"));

		initRootPanel();

		showActualView();
	}

	private void initRootPanel() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(SoftplcApplication.class
					.getResource("view/RootPanel.fxml"));
			rootLayout = loader.load();

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);

			// Give the controller access to the main app.
			RootPanelController controller = loader.getController();
			controller.setMainApp(this);

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// // Try to load last opened person file.
		// File file = getPersonFilePath();
		// if (file != null) {
		// loadPersonDataFromFile(file);
		// }
	}

	/**
	 * Application entry point.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	private void showActualView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(SoftplcApplication.class
					.getResource("view/ActualView.fxml"));

			AnchorPane actualView = loader.load();
			rootLayout.setCenter(actualView);

			ActualViewController controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 *
	 * @return Java FX primary stage reference
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * Loads PLC configuration from a file.
	 *
	 * @param file
	 *            file name
	 */
	public void loadPlcFromFile(File file) {
		// TODO Auto-generated method stub

	}
}
