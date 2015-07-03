package de.peteral.softplc.datatype;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@SuppressWarnings({ "javadoc" })
@RunWith(Parameterized.class)
public class BCDTest {

	private final byte bcd;
	private final int value;

	/* @formatter:off */
	@Parameters(name = "{index}: {0} = {1}")
	public static Iterable<Object[]> getParameters() {
		return Arrays.asList(new Object[][] {//
				{(byte)0x21, 21 },
				{(byte)0x00, 00 },
				{(byte)0x17, 17 },
		});
	}
	/* @formatter:on */

	public BCDTest(byte bcd, int value) {
		this.bcd = bcd;
		this.value = value;
	}

	@Test
	public void toBCD_Parameterized_ReturnsCorrectValue() {
		assertEquals(bcd, BCD.toBCD(value));
	}

	@Test
	public void fromBCD_Parameterized_ReturnsCorrectValue() {
		assertEquals(value, BCD.fromBCD(bcd));
	}
}
