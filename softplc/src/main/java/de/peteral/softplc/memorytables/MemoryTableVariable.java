package de.peteral.softplc.memorytables;

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
