package de.peteral.softplc.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class represents an entry in the symbol table of a CPU.
 *
 * @author peteral
 *
 */
public class Symbol {
	private final StringProperty address = new SimpleStringProperty();
	private final StringProperty name = new SimpleStringProperty();

	/**
	 * Default constructor.
	 *
	 * @param name
	 *            symbolic name
	 * @param address
	 *            address
	 */
	public Symbol(String name, String address) {
		getAddress().set(address);
		getName().set(name);
	}

	/**
	 * @return the address
	 */
	public StringProperty getAddress() {
		return address;
	}

	/**
	 * @return the name
	 */
	public StringProperty getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName().get() + " = " + getAddress().get();
	}
}
