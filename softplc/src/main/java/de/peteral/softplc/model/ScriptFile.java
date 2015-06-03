package de.peteral.softplc.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Encapsulates a JavaScript source file.
 *
 * @author peteral
 *
 */
public class ScriptFile {
	private final StringProperty fileName;
	private final StringProperty source;
	private final File file;

	/**
	 * Creates a new instance.
	 *
	 * @param fileName
	 *            file name
	 * @param file
	 */
	public ScriptFile(String fileName, File file) {
		this.file = file;
		this.source = new SimpleStringProperty();
		this.fileName = new SimpleStringProperty(fileName);
	}

	/**
	 * @return the source
	 */
	public StringProperty getSource() {
		return source;
	}

	/**
	 * @return the fileName
	 */
	public StringProperty getFileName() {
		return fileName;
	}

	/**
	 * Loads contents from disk.
	 *
	 * @throws IOException
	 */
	public void reload() throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get(file.getCanonicalPath()));
		source.set(new String(bytes));
	}

}
