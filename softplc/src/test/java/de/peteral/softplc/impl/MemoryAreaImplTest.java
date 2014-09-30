package de.peteral.softplc.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.impl.MemoryAreaImpl;
import de.peteral.softplc.model.MemoryAccessViolationException;
import de.peteral.softplc.model.MemoryArea;

@SuppressWarnings("javadoc")
public class MemoryAreaImplTest {

	private static final int SIZE = 10000;
	private static final String AREA_CODE = "M";
	private MemoryArea area;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		area = new MemoryAreaImpl(AREA_CODE, SIZE);
	}

	@Test
	public void getAreaCode_None_ReturnsAreaCode() {
		assertEquals(AREA_CODE, area.getAreaCode());
	}

	@Test
	public void writeBytes_ValidData_ReadBytesReturnsCorrectData() {
		byte[] data = { 0x01, 0x02, 0x03, 0x04 };
		area.writeBytes(20, data);

		assertArrayEquals(data, area.readBytes(20, 4));
	}

	@Test(expected = MemoryAccessViolationException.class)
	public void writeBytes_OffsetPlusLengthGreaterThanTotalSize_ThrowsException() {
		area.writeBytes(9999, new byte[] { 0x00, 0x00 });
	}

	@Test(expected = MemoryAccessViolationException.class)
	public void writeBytes_OffsetGreaterTotalSize_ThrowsException() {
		area.writeBytes(10000, new byte[] { 0x00 });
	}

	@Test(expected = MemoryAccessViolationException.class)
	public void writeBytes_OffsetNegative_ThrowsException() {
		area.writeBytes(-1, new byte[] { 0x00 });
	}

	@Test
	public void setBit_ValidDataValueTrue_ReadBytesReturnsCorrectData() {
		area.writeBytes(0, new byte[] { 0, 0, 0, 0 });

		area.setBit(2, 3, true);

		assertArrayEquals(new byte[] { 0x08 }, area.readBytes(2, 1));
	}

	@Test
	public void setBit_ValidDataValueFalse_ReadBytesReturnsCorrectData() {
		area.writeBytes(0, new byte[] { 0, 0, 0x08, 0 });

		area.setBit(2, 3, false);

		assertArrayEquals(new byte[] { 0x00 }, area.readBytes(2, 1));
	}
}
