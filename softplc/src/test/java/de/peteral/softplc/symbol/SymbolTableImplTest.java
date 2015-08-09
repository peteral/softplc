package de.peteral.softplc.symbol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import de.peteral.softplc.model.Symbol;

@SuppressWarnings("javadoc")
public class SymbolTableImplTest {

	private static final String SYMBOL = "symbol";
	private static final String ADDRESS = "address";
	private SymbolTableImpl table;

	@Before
	public void setup() {
		table = new SymbolTableImpl();
	}

	@Test
	public void getAddress_SymbolDefined_ReturnsAddress() {
		table.getAllSymbols().add(new Symbol(SYMBOL, ADDRESS));

		assertEquals(ADDRESS, table.getAddress(SYMBOL));
	}

	@Test
	public void getAddress_SymbolUndefined_ReturnsNull() {
		assertNull(table.getAddress(SYMBOL));
	}
}
