package de.peteral.softplc.memory;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.address.AddressParserFactory;
import de.peteral.softplc.datatype.DataTypeFactory;

@SuppressWarnings("javadoc")
public class MemoryIntegrationTest {
	private static final String BYTE_ADDRESS = "M,B20,4";
	private static final String DWORD_ADDRESS = "M,DW20";
	private static final String DINT_ADDRESS = "M,DI20";
	private static final String WORD_ADDRESS = "M,W20,2";
	private static final String INT_ADDRESS = "M,I20,2";
	private static final String REAL_ADDRESS = "M,REAL20";

	/* @formatter:off */
    private static final Integer[] BYTE_VALUE = new Integer[] { 0x42, 0xf6, 0xe9, 0x79 };
    private static final Integer[] WORD_VALUE = new Integer[] { 0x42f6, 0xe979 };
    private static final Short[] INT_VALUE = new Short[] { 17142, -5767 };
    private static final long DWORD_VALUE = 0x42F6E979;
    private static final int DINT_VALUE = 1123477881;
    private static final float REAL_VALUE = 123.456f;
    /* @formatter:on */

	private MemoryImpl memory;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		memory = new MemoryImpl(new AddressParserFactory(),
				new DataTypeFactory(), new MemoryAreaImpl("M", 200, false));

	}

	@Test
	public void write_ByteArray_ValuesCorrectViaAllTypes() {
		memory.write(BYTE_ADDRESS, BYTE_VALUE);

		assertMemoryCorrect();
	}

	@Test
	public void write_WordArray_ValuesCorrectViaAllTypes() {
		memory.write(WORD_ADDRESS, WORD_VALUE);

		assertMemoryCorrect();
	}

	@Test
	public void write_IntArray_ValuesCorrectViaAllTypes() {
		memory.write(INT_ADDRESS, INT_VALUE);

		assertMemoryCorrect();
	}

	@Test
	public void write_DwordValue_ValuesCorrectViaAllTypes() {
		memory.write(DWORD_ADDRESS, DWORD_VALUE);

		assertMemoryCorrect();
	}

	@Test
	public void write_DintValue_ValuesCorrectViaAllTypes() {
		memory.write(DINT_ADDRESS, DINT_VALUE);

		assertMemoryCorrect();
	}

	@Test
	public void write_RealValue_ValuesCorrectViaAllTypes() {
		memory.write(REAL_ADDRESS, REAL_VALUE);

		assertMemoryCorrect();
	}

	private void assertMemoryCorrect() {
		assertEquals(true, memory.read("M,X23.0"));
		assertEquals(false, memory.read("M,X23.1"));
		assertEquals(false, memory.read("M,X23.2"));
		assertEquals(true, memory.read("M,X23.3"));
		assertEquals(true, memory.read("M,X23.4"));
		assertEquals(true, memory.read("M,X23.5"));
		assertEquals(true, memory.read("M,X23.6"));
		assertEquals(false, memory.read("M,X23.7"));

		assertArrayEquals(BYTE_VALUE, (Integer[]) memory.read(BYTE_ADDRESS));
		assertArrayEquals(WORD_VALUE, (Integer[]) memory.read(WORD_ADDRESS));
		assertEquals(DWORD_VALUE, memory.read(DWORD_ADDRESS));
		assertEquals(REAL_VALUE, memory.read(REAL_ADDRESS));
		assertArrayEquals(INT_VALUE, (Short[]) memory.read(INT_ADDRESS));
		assertEquals(DINT_VALUE, memory.read(DINT_ADDRESS));
	}

	@Test
	public void write_BitToTrueAndFalse_ReturnsFalse() {
		memory.write("M,X23.5", true);
		assertEquals(true, memory.read("M,X23.5"));

		memory.write("M,X23.5", false);
		assertEquals(false, memory.read("M,X23.5"));
	}
}
