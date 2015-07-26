package de.peteral.softplc.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a memory snapshot in the data model.
 * <p>
 * Provides means to load memory snapshot to memory and save it from memory to a
 * file.
 *
 * @author peteral
 *
 */
public class MemorySnapshot {

	private final BooleanProperty isDefault = new SimpleBooleanProperty();
	private final StringProperty fileName = new SimpleStringProperty();

	/**
	 * Creates a new instance.
	 *
	 * @param isDefault
	 *            - should this snapshot be loaded on application startup?
	 * @param file
	 *            file name - absolute paths and paths relative to the
	 *            configuration file supported.
	 */
	public MemorySnapshot(boolean isDefault, String file) {
		this.isDefault.set(isDefault);
		this.fileName.set(file);
	}

	/**
	 *
	 * @return default memory snapshot is loaded during application startup
	 */
	public BooleanProperty isDefault() {
		return isDefault;
	}

	/**
	 *
	 * @return name of the file containing the memory snapshot
	 */
	public StringProperty getFileName() {
		return fileName;
	}

	/**
	 * Loads memory snapshot into memory.
	 *
	 * @param memory
	 */
	public void load(Memory memory) {

	}

	/**
	 * Saves memory snapshot from memory to a file.
	 * 
	 * @param memory
	 */
	public void save(Memory memory) {

	}

}