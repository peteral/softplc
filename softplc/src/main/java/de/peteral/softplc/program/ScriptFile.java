package de.peteral.softplc.program;

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

	/**
	 * Creates a new instance.
	 *
	 * @param fileName
	 *            file name
	 * @param source
	 *            file content
	 */
	public ScriptFile(String fileName, String source) {
		this.source = new SimpleStringProperty(source);
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

}
