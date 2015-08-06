package de.peteral.softplc.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents one variable in a {@link MemoryTable}.
 *
 * @author peteral
 *
 */
public class MemoryTableVariable {
	private final StringProperty variable = new SimpleStringProperty();
	private final StringProperty currentValue = new SimpleStringProperty();
	private final StringProperty newValue = new SimpleStringProperty();
	private final StringProperty comment = new SimpleStringProperty();

	/**
	 * This constructor is used during configuration parsing.
	 *
	 * @param name
	 *            variable name
	 * @param newValue
	 *            new value
	 */
	public MemoryTableVariable(String name, String newValue, String comment) {
		variable.set(name);
		this.newValue.set(newValue);
		this.comment.set(comment);
	}

	/**
	 * Default constructor.
	 */
	public MemoryTableVariable() {

	}

	/**
	 * This constructor is used by memory table update tasks.
	 *
	 * @param name
	 * @param newValue
	 */
	public MemoryTableVariable(String name, String newValue) {
		this(name, newValue, "");
	}

	/**
	 * @return the variable
	 */
	public StringProperty getVariable() {
		return variable;
	}

	/**
	 * @return the currentValue
	 */
	public StringProperty getCurrentValue() {
		return currentValue;
	}

	/**
	 * @return the newValue
	 */
	public StringProperty getNewValue() {
		return newValue;
	}

	/**
	 * @return the comment
	 */
	public StringProperty getComment() {
		return comment;
	}
}
