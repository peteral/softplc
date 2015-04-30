package eisenmann.connector.plc.ra.virtualplc.telegram.s7;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class S7BytesTest 
{
	@Test
	public void getBitValueAllFalse()
	{
		byte value = 0;
		for(int i=0; i<8; i++)
		{
			assertEquals(false, S7Bytes.getBitValue(value, i));
		}
	}

	@Test
	public void getBitValueAllTrue()
	{
		byte value = (byte) 0xFF;
		for(int i=0; i<8; i++)
		{
			assertEquals(true, S7Bytes.getBitValue(value, i));
		}
	}

	@Test
	public void getBitValueZeroIsTrue()
	{
		byte value = (byte) 0x01;
		assertEquals(true, S7Bytes.getBitValue(value, 0));
		assertEquals(false, S7Bytes.getBitValue(value, 1));
		assertEquals(false, S7Bytes.getBitValue(value, 2));
		assertEquals(false, S7Bytes.getBitValue(value, 3));
		assertEquals(false, S7Bytes.getBitValue(value, 4));
		assertEquals(false, S7Bytes.getBitValue(value, 5));
		assertEquals(false, S7Bytes.getBitValue(value, 6));
		assertEquals(false, S7Bytes.getBitValue(value, 7));

	}

	@Test
	public void getBitValue()
	{
		byte value = (byte) 0x80;
		assertEquals(true, S7Bytes.getBitValue(value, 7));
	}
}
