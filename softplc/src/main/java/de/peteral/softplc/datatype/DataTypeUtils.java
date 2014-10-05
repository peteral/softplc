package de.peteral.softplc.datatype;

/**
 * Data type utilities.
 *
 * @author peteral
 *
 */
public final class DataTypeUtils {

	private DataTypeUtils() {

	}

	/**
	 * Converts unsigned 8 bit stored within byte into integer value.
	 *
	 * @param b
	 * @return integer
	 */
	public static int byteToInt(byte b) {
		return (b < 0) ? 128 + (b & 0x7F) : b;
	}
}
