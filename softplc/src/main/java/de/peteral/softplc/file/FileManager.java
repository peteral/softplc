package de.peteral.softplc.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.prefs.Preferences;

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
import de.peteral.softplc.view.error.ErrorDialog;

/**
 * Abstraction for file operations
 *
 * @author peteral
 *
 */
public class FileManager {
	private static final String FILE_PATH = "filePath";
	private Plc plc;
	private final Consumer<File> fileConsumer;
	private final Consumer<Plc> plcConsumer;

	/**
	 * Initialize new instance.
	 *
	 * @param fileConsumer
	 * @param plcConsumer
	 */
	public FileManager(Consumer<File> fileConsumer, Consumer<Plc> plcConsumer) {
		this.fileConsumer = fileConsumer;
		this.plcConsumer = plcConsumer;

	}

	/**
	 *
	 * @return last opened / saved file
	 */
	public File getLastOpenedFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(FileManager.class);
		String filePath = prefs.get(FILE_PATH, null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}

	/**
	 * Loads PLC configuration from a file.
	 *
	 * @param file
	 *            file name
	 */
	public void loadPlcFromFile(File file) {
		try {
			if (plc != null) {
				plc.stop();
			}

			Plc newPlc = new PlcFactory().create(file.getAbsolutePath());
			setPlc(newPlc);
			setLastOpenedFilePath(file);
			plc.start();
		} catch (PlcFactoryException e) {
			ErrorDialog.show("Failed loading configuration [" + file.getPath()
					+ "]", e);
		}
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
			plc.setPath(file);

			// TODO handling for script files - do we copy them?
		} catch (ParserConfigurationException | IOException
				| TransformerException e) {

			ErrorDialog.show("Failed saving file", e);
		}
	}

	/**
	 * creates new empty configuration
	 */
	public void newPlc() {
		if (plc != null) {
			plc.stop();
		}

		Plc newPlc = new PlcFactory().createNew();
		setPlc(newPlc);
		setLastOpenedFilePath(null);
		plc.start();
	}

	private void setLastOpenedFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(FileManager.class);
		if (file != null) {
			prefs.put(FILE_PATH, file.getPath());
		} else {
			prefs.remove(FILE_PATH);
		}

		fileConsumer.accept(file);
	}

	/**
	 * loads last opened / saved file
	 */
	public void loadLastFile() {
		File file = getLastOpenedFilePath();
		if (file != null) {
			loadPlcFromFile(file);
		} else {
			newPlc();
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
		plcConsumer.accept(plc);
	}

}
