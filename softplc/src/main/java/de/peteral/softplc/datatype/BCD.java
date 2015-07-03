package de.peteral.softplc.datatype;

/**
 * BCD conversion utility class.
 *
 * @author peteral
 *
 */
public class BCD {

	/**
	 * Converts decimal value to BCD code.
	 *
	 * @param value
	 *            only handled as byte!
	 * @return BCD coded value
	 */
	public static byte toBCD(int value) {
		int low = value % 10;
		int high = value / 10;
		return (byte) ((high << 4) + low);
	}

	/**
	 * Converts BCD coded value to decimal.
	 *
	 * @param bcd
	 * @return decimal value
	 */
	public static int fromBCD(byte bcd) {
		int high = (bcd & 0xf0) >> 4;
		int low = bcd & 0x0f;

		return (10 * high) + low;
	}
}
