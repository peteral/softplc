package de.peteral.softplc.memorytables;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Memory tables can be configured in UI to observe and interact with PLC
 * memory.
 *
 * @author peteral
 *
 */
public class MemoryTable {
	private final StringProperty name = new SimpleStringProperty();

	private final ObservableList<MemoryTableVariable> variables = FXCollections
			.observableArrayList();

	/**
	 * @return the variables
	 */
	public ObservableList<MemoryTableVariable> getVariables() {
		return variables;
	}

	/**
	 * @return the name
	 */
	public StringProperty getName() {
		return name;
	}
}
