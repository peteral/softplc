package de.peteral.softplc.datatype;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.address.ParsedAddress;

@SuppressWarnings({ "javadoc" })
@RunWith(Parameterized.class)
public class DataTypeFactoryTest {

	private final String stringValue;

	/* @formatter:off */
	@Parameters(name = "{index}: {0} = {1}")
	public static Iterable<Object[]> getParameters() {
		return Arrays.asList(new Object[][] {//
				{ new ParsedAddress("DB100,C10:20"), "Hallo", new byte[] { 'H', 'a', 'l', 'l', 'o', 0x00 }, 20, 1, 0, 1, "Hallo"},
				{ new ParsedAddress("DB100,C10:10,2"), new String[] { "Hallo", "abcd" }, new byte[] { 'H', 'a', 'l', 'l', 'o', 0x00, 0x00, 0x00, 0x00, 0x00, 'a', 'b', 'c', 'd', 0x00 }, 20, 1, 0, 2, "[Hallo, abcd]"},
				{ new ParsedAddress("M100,B20"), 20, new byte[] { 20 }, 1, 1, 0, 1 , "20"},
				{ new ParsedAddress("M100,B20"), 250, new byte[] { (byte) 250 }, 1, 1, 0, 1, "250"},
				{ new ParsedAddress("M100,B20,2"), new Integer[] { 10, 20 }, new byte[] { 10, 20 }, 2, 1, 0, 2 , "[10, 20]"},
				{ new ParsedAddress("M100,W20"), 20, new byte[] { 0, 20 }, 2, 2, 0, 1 , "20"},
				{ new ParsedAddress("M100,W20,2"), new Integer[] { 10, 20 }, new byte[] { 0, 10, 0, 20 }, 4, 2, 0, 2, "[10, 20]" },
				{ new ParsedAddress("M100,B20,4"), new Integer[] { 0x42, 0xf6, 0xe9, 0x79 }, new byte[] { 0x42, (byte) 0xf6, (byte) 0xe9, 0x79 }, 4, 1, 0, 4, "[66, -10, -23, 121]" },
				{ new ParsedAddress("M100,DT20"), LocalDateTime.of(2015, 7, 3, 21, 20, 50, 250*1000*1000), new byte[] { 0x15, 0x07, 0x03, 0x21, 0x20, 0x50, 0x25, 0x06 }, 8, 8, 0, 1, "2015-07-03T21:20:50.250" },
		});
	}
	/* @formatter:on */

	public DataTypeFactoryTest(ParsedAddress address, Object value,
			byte[] bytes, int byteArraySize, int elementSize, int headerSize,
			int count, String stringValue) {
		this.address = address;
		this.value = value;
		this.bytes = bytes;
		this.byteArraySize = byteArraySize;
		this.elementSize = elementSize;
		this.headerSize = headerSize;
		this.count = count;
		this.stringValue = stringValue;
	}

	private final int byteArraySize;
	private final int elementSize;
	private final int headerSize;
	private final int count;
	private final byte[] bytes;
	private final ParsedAddress address;
	private final Object value;
	private static final byte[] INVALID_BYTES = {};
	private DataTypeFactory factory;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		factory = new DataTypeFactory();
	}

	@Test
	public void getElementSize_None_Parameterized() {
		assertEquals(elementSize, factory.getElementSize(address));
	}

	@Test
	public void getHeaderSize_None_Parameterized() {
		assertEquals(headerSize, factory.getHeaderSize(address));
	}

	@Test
	public void fromBytes_ValidBytes_ReturnsCorrectValue() {
		if (count > 1) {
			assertArrayEquals((Object[]) value,
					(Object[]) factory.fromBytes(bytes, address));
		} else {
			assertEquals(value, factory.fromBytes(bytes, address));
		}
	}

	@Test(expected = DataTypeException.class)
	public void fromBytes_InvalidBytes_ThrowsException() {
		factory.fromBytes(INVALID_BYTES, address);
	}

	@Test
	public void toBytes_ValidValue_ReturnsCorrectBytes() {
		byte[] result = factory.toBytes(value, address);
		assertEquals(byteArraySize, result.length);

		for (int i = 0; i < bytes.length; i++) {
			assertEquals("Position [" + i + "]: " + bytes[i] + " != "
					+ result[i], bytes[i], result[i]);
		}
	}

	@Test
	public void parseToBytes_ValidValue_ReturnsCorrectBytes() {
		byte[] result = factory.parseToBytes(stringValue, address);
		assertEquals(byteArraySize, result.length);

		for (int i = 0; i < bytes.length; i++) {
			assertEquals("Position [" + i + "]: " + bytes[i] + " != "
					+ result[i], bytes[i], result[i]);
		}
	}
}
