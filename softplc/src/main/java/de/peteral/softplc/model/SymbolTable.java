package de.peteral.softplc.model;

import javafx.collections.ObservableList;

/**
 * This interface defines a symbol table of a CPU.
 * <p>
 * A symbol table translates symbolic names to physical addresses.
 *
 * @author peteral
 *
 */
public interface SymbolTable {
	/**
	 *
	 * @return observable list of all symbols - elements can be added, removed
	 *         and modified
	 */
	ObservableList<Symbol> getAllSymbols();

	/**
	 * Translates symbolic name to an address
	 *
	 * @param name
	 *            symbolic name
	 * @return hardware address, null when symbol is not defined
	 */
	String getAddress(String name);
}
