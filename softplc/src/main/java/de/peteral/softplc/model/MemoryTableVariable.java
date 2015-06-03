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

	/**
	 * This constructor is used during configuration parsing.
	 *
	 * @param name
	 *            variable name
	 * @param newValue
	 *            new value
	 */
	public MemoryTableVariable(String name, String newValue) {
		variable.set(name);
		this.newValue.set(newValue);
	}

	/**
	 * Default constructor.
	 */
	public MemoryTableVariable() {

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
}
