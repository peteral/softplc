package de.peteral.softplc.dataType;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.address.ParsedAddress;

@SuppressWarnings({ "javadoc", "rawtypes" })
@RunWith(Parameterized.class)
public class DataTypeFactoryTest {

	/* @formatter:off */
	@Parameters(name = "{index}: {0} = {1}")
	public static Iterable<Object[]> getParameters() {
		return Arrays.asList(new Object[][] {//
				{ new ParsedAddress("DB100,C10:20"), "Hallo", new byte[] { 'H', 'a', 'l', 'l', 'o', 0x00 }, 20, String.class, 1, 0, 1 },
				{ new ParsedAddress("DB100,C10:10,2"), new String[] { "Hallo", "abcd" }, new byte[] { 'H', 'a', 'l', 'l', 'o', 0x00, 0x00, 0x00, 0x00, 0x00, 'a', 'b', 'c', 'd', 0x00 }, 20, String.class, 1, 0, 2 },
				{ new ParsedAddress("M100,B20"), 20.0, new byte[] { 20 }, 1, Byte.class, 1, 0, 1 },
				{ new ParsedAddress("M100,B20,2"), new Double[] { 10.0, 20.0 }, new byte[] { 10, 20 }, 2, Byte.class, 1, 0, 2 },
				{ new ParsedAddress("M100,W20"), 20.0, new byte[] { 0, 20 }, 2, Integer.class, 2, 0, 1 },
				{ new ParsedAddress("M100,W20,2"), new Double[] { 10.0, 20.0 }, new byte[] { 0, 10, 0, 20 }, 4, Integer.class, 2, 0, 2 },
		});
	}
	/* @formatter:on */

	public DataTypeFactoryTest(ParsedAddress address, Object value,
			byte[] bytes, int byteArraySize, Class type, int elementSize,
			int headerSize, int count) {
		this.address = address;
		this.value = value;
		this.bytes = bytes;
		this.byteArraySize = byteArraySize;
		this.type = type;
		this.elementSize = elementSize;
		this.headerSize = headerSize;
		this.count = count;
	}

	private final int byteArraySize;
	private final Class type;
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
	public void getType_None_Parameterized() {
		assertEquals(type, factory.getType(address));
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
}
