package de.peteral.softplc.model;

import java.time.LocalDate;
import java.util.logging.Level;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Error log entry for display in UI.
 *
 * Last X logged entries are remembered.
 *
 * @author peteral
 *
 */
public class ErrorLogEntry {
	private final ObjectProperty<LocalDate> timestamp = new SimpleObjectProperty<>();
	private final StringProperty level = new SimpleStringProperty();
	private final StringProperty module = new SimpleStringProperty();
	private final StringProperty message = new SimpleStringProperty();

	/**
	 * Initializes new instance.
	 *
	 * @param level
	 * @param module
	 * @param message
	 */
	public ErrorLogEntry(Level level, String module, String message) {
		this.level.set(level.getName());
		this.module.set(module);
		this.timestamp.set(LocalDate.now());
		this.message.set(message);
	}

	/**
	 * @return the timestamp
	 */
	public ObjectProperty<LocalDate> getTimestamp() {
		return timestamp;
	}

	/**
	 * @return the level
	 */
	public StringProperty getLevel() {
		return level;
	}

	/**
	 * @return the module
	 */
	public StringProperty getModule() {
		return module;
	}

	/**
	 * @return the message
	 */
	public StringProperty getMessage() {
		return message;
	}
}
