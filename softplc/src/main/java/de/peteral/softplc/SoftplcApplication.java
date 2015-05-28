package de.peteral.softplc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import de.peteral.softplc.model.Plc;
import de.peteral.softplc.plc.PlcFactory;
import de.peteral.softplc.plc.PlcFactoryException;
import de.peteral.softplc.plc.PlcTransformer;
import de.peteral.softplc.view.ActualViewController;
import de.peteral.softplc.view.RootPanelController;

/**
 * Java FX application entry point.
 *
 * @author peteral
 *
 */
public class SoftplcApplication extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private ActualViewController actualViewController;
	private Plc plc;
	private RootPanelController rootPanelController;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		setTitle("");

		this.primaryStage.getIcons().add(
				new Image(SoftplcApplication.class
						.getResourceAsStream("/images/softplc_32.png")));

		this.primaryStage.setOnCloseRequest(event -> System.exit(0));

		initRootPanel();

		showActualView();

		loadLastFile();

	}

	private void setTitle(String appendix) {
		primaryStage.setTitle("Softplc ["
				+ SoftplcApplication.class.getPackage()
						.getImplementationVersion() + "] " + appendix);
	}

	private void loadLastFile() {
		File file = getLastOpenedFilePath();
		if (file != null) {
			loadPlcFromFile(file);
		} else {
			newPlc();
		}
	}

	private void initRootPanel() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(SoftplcApplication.class
					.getResource("view/RootPanel.fxml"));
			rootLayout = loader.load();

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);

			rootPanelController = loader.getController();
			rootPanelController.setMainApp(this);

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @return last opened / saved file
	 */
	public File getLastOpenedFilePath() {
		Preferences prefs = Preferences
				.userNodeForPackage(SoftplcApplication.class);
		String filePath = prefs.get("filePath", null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}

	private void setLastOpenedFilePath(File file) {
		Preferences prefs = Preferences
				.userNodeForPackage(SoftplcApplication.class);
		if (file != null) {
			prefs.put("filePath", file.getPath());

			// Update the stage title.
			setTitle(" - " + file.getName());
		} else {
			prefs.remove("filePath");

			// Update the stage title.
			setTitle("");
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

	private void showActualView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(SoftplcApplication.class
					.getResource("view/ActualView.fxml"));

			AnchorPane actualView = loader.load();
			rootLayout.setCenter(actualView);

			actualViewController = loader.getController();
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
		try {
			Plc newPlc = new PlcFactory().create(file.getAbsolutePath());
			setPlc(newPlc);
			setLastOpenedFilePath(file);
		} catch (PlcFactoryException e) {
			e.printStackTrace();
			// TODO error dialog
		}
	}

	/**
	 * @return the plc
	 */
	public Plc getPlc() {
		return plc;
	}

	/**
	 * @param plc
	 *            the plc to set
	 */
	public void setPlc(Plc plc) {
		this.plc = plc;
		actualViewController.setPlc(getPlc());
	}

	/**
	 * Saves current configuration to a file
	 *
	 * @param file
	 */
	public void save(File file) {
		try {
			Document doc = new PlcTransformer().transform(getPlc());
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
					"softplc.dtd");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "4");
			FileWriter writer = new FileWriter(file);
			transformer.transform(new DOMSource(doc), new StreamResult(writer));

			setLastOpenedFilePath(file);

			// TODO handling for script files - do we copy them?
		} catch (ParserConfigurationException | IOException
				| TransformerException e) {
			// TODO - error dialog
			e.printStackTrace();
		}
	}

	/**
	 * creates new empty configuration
	 */
	public void newPlc() {
		Plc newPlc = new PlcFactory().createNew();
		setPlc(newPlc);
		setLastOpenedFilePath(null);
	}
}
