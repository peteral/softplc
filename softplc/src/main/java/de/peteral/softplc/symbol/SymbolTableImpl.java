package de.peteral.softplc.symbol;

import de.peteral.softplc.model.Symbol;
import de.peteral.softplc.model.SymbolTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * {@link SymbolTable} implementation.
 * <p>
 * not thread safe
 *
 * @author peteral
 *
 */
// TODO - possible optimization - listen to symbols list changes and update a
// map, use this map to resolve addresses
public class SymbolTableImpl implements SymbolTable {
	private final ObservableList<Symbol> symbols = FXCollections.observableArrayList();

	@Override
	public ObservableList<Symbol> getAllSymbols() {
		return symbols;
	}

	@Override
	public String getAddress(String name) {
		for (Symbol symbol : symbols) {
			if (symbol.getName().get().equals(name)) {
				return symbol.getAddress().get();
			}
		}

		return null;
	}

}
