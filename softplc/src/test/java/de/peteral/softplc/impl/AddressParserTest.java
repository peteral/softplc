package de.peteral.softplc.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings({ "javadoc", "rawtypes" })
@RunWith(Parameterized.class)
public class AddressParserTest {

	@Parameters(name = "{index}: {0}")
	public static Iterable<Object[]> getParameters() {
		return Arrays.asList(new Object[][] { //
				{ "M,W100", "M", 100, 0, "W", 2, 0, 1, 2 }, //
				{ "M,STRING100,10", "M", 100, 0, "STRING", 1, 2, 10, 12 }, //
				{ "DB100,C100,20", "DB100", 100, 0, "C", 1, 0, 20, 20 }, //
				{ "M,X100.5", "M", 100, 5, "X", 1, 0, 1, 1 } });
	}

	private AddressParser parser;
	private final String address;
	private final String areaCode;
	private final int offset;
	private final int size;
	private final int bitNumber;
	@Mock
	private DataTypeFactory dataTypeFactory;
	private final String typeName;
	private final int count;
	private final int elementSize;
	private final int headerSize;

	public AddressParserTest(String address, String areaCode, int offset,
			int bitNumber, String typeName, int elementSize, int headerSize,
			int count, int size) {
		super();
		this.address = address;
		this.areaCode = areaCode;
		this.offset = offset;
		this.bitNumber = bitNumber;
		this.typeName = typeName;
		this.elementSize = elementSize;
		this.headerSize = headerSize;
		this.count = count;
		this.size = size;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		when(dataTypeFactory.getElementSize(typeName)).thenReturn(elementSize);
		when(dataTypeFactory.getHeaderSize(typeName)).thenReturn(
				headerSize);

		parser = new AddressParser(dataTypeFactory, address);
	}

	@Test
	public void getAreaCode_None_ReturnsCorrectAreaCode() {
		assertEquals(areaCode, parser.getAreaCode());
	}

	@Test
	public void getOffset_None_ReturnsCorrectOffset() {
		assertEquals(offset, parser.getOffset());
	}

	@Test
	public void getType_None_ReturnsCorrectDataType() {
		assertEquals(typeName, parser.getTypeName());
	}

	@Test
	public void getSize_None_ReturnsCorrectSize() {
		assertEquals(size, parser.getSize());
	}

	@Test
	public void getBitNumber_None_ReturnsCorrectBitNumber() {
		assertEquals(bitNumber, parser.getBitNumber());
	}

	@Test
	public void getCount_None_ReturnsCorrectCount() {
		assertEquals(count, parser.getCount());
	}
}
