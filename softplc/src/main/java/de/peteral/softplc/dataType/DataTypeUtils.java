package de.peteral.softplc.dataType;

/**
 * Data type utilities.
 *
 * @author peteral
 *
 */
public class DataTypeUtils {

	/**
	 * Converts unsigned 8 bit stored within byte into integer value.
	 *
	 * @param b
	 * @return
	 */
	public static int byteToInt(byte b) {
		return (b < 0) ? 128 + (b & 0x7F) : b;
	}
}
