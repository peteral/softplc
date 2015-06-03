package de.peteral.softplc;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import de.peteral.softplc.file.FileManager;
import de.peteral.softplc.view.ApplicationController;
import de.peteral.softplc.view.CpuTableViewController;

/**
 * Java FX application entry point.
 *
 * @author peteral
 *
 */
public class SoftplcApplication extends Application {

	private static final String VERSION = "1.2.2";
	private Stage primaryStage;
	private BorderPane applicationPane;
	private CpuTableViewController cpuTableViewController;
	private ApplicationController applicationController;
	private final FileManager fileManager = new FileManager(file -> {
		if (file != null) {
			// Update the stage title.
			setTitle(" - " + file.getName());
		} else {
			// Update the stage title.
			setTitle("");
		}
	}, plc -> cpuTableViewController.setPlc(plc));
	private static final Logger LOGGER = Logger.getLogger("application");

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		setTitle("");

		this.primaryStage.getIcons().add(
				new Image(SoftplcApplication.class
						.getResourceAsStream("/images/softplc_32.png")));

		this.primaryStage.setOnCloseRequest(event -> System.exit(0));

		initApplicationPane();

		showCpuTableView();

		fileManager.loadLastFile();

	}

	private void setTitle(String appendix) {
		primaryStage.setTitle("Softplc [" + VERSION + "] " + appendix);
	}

	private void initApplicationPane() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(SoftplcApplication.class
					.getResource("view/Application.fxml"));
			applicationPane = loader.load();

			Scene scene = new Scene(applicationPane);
			primaryStage.setScene(scene);

			applicationController = loader.getController();
			applicationController.setFileManager(fileManager);
			applicationController.setStage(primaryStage);

			primaryStage.show();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Failed loading Application.fxml:", e);
		}
	}

	/**
	 * Application entry point.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	private void showCpuTableView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(SoftplcApplication.class
					.getResource("view/CpuTableView.fxml"));

			AnchorPane actualView = loader.load();
			applicationPane.setCenter(actualView);

			cpuTableViewController = loader.getController();
			cpuTableViewController.setPrimaryStage(primaryStage);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Failed loading CpuTableView.fxml:", e);
		}

	}

}
