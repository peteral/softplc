package de.peteral.softplc.memory;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.address.AddressParserException;
import de.peteral.softplc.address.AddressParserFactory;
import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.datatype.DataTypeFactory;
import de.peteral.softplc.model.Memory;
import de.peteral.softplc.model.MemoryAccessViolationException;
import de.peteral.softplc.model.MemoryArea;
import de.peteral.softplc.model.Symbol;
import javafx.beans.property.SimpleStringProperty;

@SuppressWarnings("javadoc")
public class MemoryImplTest {
	private static final int OFFSET = 123;
	private static final String DB100 = "DB100";
	private static byte[] ANY_DATA = new byte[] { 0x01, 0x02, 0x03, 0x04 };

	private Memory memory;
	@Mock
	private MemoryArea areaM;
	@Mock
	private MemoryArea areaDb100;
	@Mock
	private AddressParserFactory addressParserFactory;
	@Mock
	private ParsedAddress addressParser;
	@Mock
	private DataTypeFactory dataTypeFactory;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		when(areaM.getAreaCode()).thenReturn(new SimpleStringProperty("M"));
		when(areaDb100.getAreaCode()).thenReturn(new SimpleStringProperty(DB100));
		when(addressParserFactory.parse(anyString())).thenReturn(addressParser);

		memory = new MemoryImpl(addressParserFactory, dataTypeFactory, areaM, areaDb100);
	}

	@Test
	public void getMemoryArea_ExistingMemoryArea_ReturnsMemoryArea() {
		assertEquals(areaDb100, memory.getMemoryArea(DB100));
	}

	@Test(expected = MemoryAccessViolationException.class)
	public void getMemoryArea_InvalidMemoryArea_ThrowsException() {
		memory.getMemoryArea("xxx");
	}

	@Test
	public void readBytes_ExistingMemoryArea_DelegatesToMemoryArea() {
		when(areaDb100.readBytes(OFFSET, ANY_DATA.length)).thenReturn(ANY_DATA);

		assertArrayEquals(ANY_DATA, memory.readBytes(DB100, OFFSET, ANY_DATA.length));
	}

	@Test
	public void writeBytes_ExistingMemoryArea_DelegatesToMemoryArea() {
		memory.writeBytes(DB100, OFFSET, ANY_DATA);

		verify(areaDb100).writeBytes(OFFSET, ANY_DATA);
	}

	@Test
	public void read_ValidArea_ReturnsCorrectResult() {
		String address = "M,W100";

		when(addressParser.getAreaCode()).thenReturn("M");
		when(addressParser.getOffset()).thenReturn(100);
		when(addressParser.getTypeName()).thenReturn("W");
		when(addressParser.getSize()).thenReturn(2);
		when(addressParser.getCount()).thenReturn(1);

		byte[] bytes = {};

		when(areaM.readBytes(100, 2)).thenReturn(bytes);
		when(dataTypeFactory.fromBytes(bytes, addressParser)).thenReturn(500.0);
		when(dataTypeFactory.getTotalSize(addressParser)).thenReturn(2);

		assertEquals(500.0, memory.read(address));
	}

	@Test
	public void write_ValidData_WritesCorrectBytes() {
		String address = "M,W100";

		when(addressParser.getAreaCode()).thenReturn("M");
		when(addressParser.getOffset()).thenReturn(100);
		when(addressParser.getTypeName()).thenReturn("W");
		when(addressParser.getSize()).thenReturn(2);
		when(addressParser.getCount()).thenReturn(1);

		byte[] bytes = {};

		when(dataTypeFactory.toBytes(2, addressParser)).thenReturn(bytes);

		memory.write(address, 2);

		verify(areaM).writeBytes(100, bytes);
	}

	@Test
	public void getBit_TestData_ReturnsCorrectValue() {
		memory = new MemoryImpl(new AddressParserFactory(), new DataTypeFactory(), new MemoryAreaImpl("M", 100, false));

		memory.writeBytes("M", 1, new byte[] { (byte) 128 });

		assertTrue(memory.getBit("M,X1.7"));
	}

	@Test
	public void setBit_TestData_WritesCorrectValue() {
		memory = new MemoryImpl(new AddressParserFactory(), new DataTypeFactory(), new MemoryAreaImpl("M", 100, false));

		memory.setBit("M,X1.7", true);

		assertEquals(128, memory.read("M,B1"));
	}

	@Test
	public void read_KnownSymbol_ReadsCorrectValue() {
		memory = new MemoryImpl(new AddressParserFactory(), new DataTypeFactory(), new MemoryAreaImpl("M", 100, false));

		memory.write("M,W10", 20);
		memory.getSymbolTable().getAllSymbols().add(new Symbol("a", "M,W10"));

		assertEquals(20, memory.read("a"));
	}

	@Test(expected = AddressParserException.class)
	public void read_UnknownSymbol_ThrowsException() {
		memory = new MemoryImpl(new AddressParserFactory(), new DataTypeFactory(), new MemoryAreaImpl("M", 100, false));

		memory.read("a");
	}

	@Test(expected = AddressParserException.class)
	public void write_UnknownSymbol_ThrowsException() {
		memory = new MemoryImpl(new AddressParserFactory(), new DataTypeFactory(), new MemoryAreaImpl("M", 100, false));

		memory.write("a", 20);
	}

	@Test
	public void write_KnownSymbol_ReadsCorrectValue() {
		memory = new MemoryImpl(new AddressParserFactory(), new DataTypeFactory(), new MemoryAreaImpl("M", 100, false));

		memory.getSymbolTable().getAllSymbols().add(new Symbol("a", "M,W10"));
		memory.write("a", 20);

		assertEquals(20, memory.read("M,W10"));
	}

	@Test(expected = AddressParserException.class)
	public void parse_UnknownSymbol_ThrowsException() {
		memory = new MemoryImpl(new AddressParserFactory(), new DataTypeFactory(), new MemoryAreaImpl("M", 100, false));

		memory.parse("a", "20");
	}

	@Test
	public void parse_KnownSymbol_ReadsCorrectValue() {
		memory = new MemoryImpl(new AddressParserFactory(), new DataTypeFactory(), new MemoryAreaImpl("M", 100, false));

		memory.getSymbolTable().getAllSymbols().add(new Symbol("a", "M,W10"));
		memory.parse("a", "20");

		assertEquals(20, memory.read("M,W10"));
	}
}
