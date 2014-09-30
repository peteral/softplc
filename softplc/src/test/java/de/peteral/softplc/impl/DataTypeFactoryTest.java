package de.peteral.softplc.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.MockitoAnnotations;

@SuppressWarnings({ "javadoc", "rawtypes" })
@RunWith(Parameterized.class)
public class DataTypeFactoryTest {

	private final int count;

	@Parameters(name = "{index}: {0} = {5}")
	public static Iterable<Object[]> getParameters() {
		return Arrays.asList(new Object[][] {//
						{ "B", Byte.class, 1, 0, new byte[] { 0x05 }, (byte) 5,
								1 }, //
					{ "C", String.class, 1, 0,
								new byte[] { 'a', 'b', 'c' }, "abc", 1 }, //
						{ "B", Byte.class, 1, 0, new byte[] { 0x05, 0x06 },
								new Byte[] { 0x05, 0x06 }, 2 } //
				});
	}

	public DataTypeFactoryTest(String typeName, Class type, int elementSize,
			int elementHeaderSize, byte[] validBytes, Object value, int count) {
		this.typeName = typeName;
		this.type = type;
		this.elementSize = elementSize;
		this.elementHeaderSize = elementHeaderSize;
		this.validBytes = validBytes;
		this.value = value;
		this.count = count;

	}

	private static final byte[] INVALID_BYTES = {};
	private DataTypeFactory factory;
	private final String typeName;
	private final Class type;
	private final int elementSize;
	private final int elementHeaderSize;
	private final byte[] validBytes;
	private final Object value;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		factory = new DataTypeFactory();
	}

	@Test
	public void getType_None_Parameterized() {
		assertEquals(type, factory.getType(typeName));
	}

	@Test
	public void getElementSize_None_Parameterized() {
		assertEquals(elementSize, factory.getElementSize(typeName));
	}

	@Test
	public void getElementHeaderSize_None_Parameterized() {
		assertEquals(elementHeaderSize, factory.getHeaderSize(typeName));
	}

	@Test
	public void fromBytes_ValidBytes_ReturnsCorrectValue() {
		if (count > 1) {
			assertArrayEquals((Object[]) value,
					(Object[]) factory.fromBytes(validBytes, typeName, count));
		} else {
			assertEquals(value, factory.fromBytes(validBytes, typeName, count));
		}
	}

	@Test(expected = DataTypeException.class)
	public void fromBytes_InvalidBytes_ThrowsException() {
		factory.fromBytes(INVALID_BYTES, typeName, count);
	}

	@Test
	public void toBytes_ValidValue_ReturnsCorrectBytes() {
		assertArrayEquals(validBytes, factory.toBytes(value, typeName, count));
	}
}
