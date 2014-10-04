package de.peteral.softplc.model;

import de.peteral.softplc.address.ParsedAddress;

/**
 * Classes implementing this interface are responsible for converting of java
 * types to byte arrays and vice versa.
 * <p>
 * Only converts a single value does not handle arrays.
 * <p>
 * All number converters work with double as it is the only type used by
 * javascript.
 *
 * @param <T>
 *
 * @author peteral
 */
public interface Converter<T> {
	/**
	 * Converts java type to byte array
	 *
	 * @param value
	 *            the java value to be converted
	 * @param address
	 * @param buffer
	 *            byte buffer
	 * @param offset
	 */
	void toBytes(T value, ParsedAddress address, byte[] buffer, int offset);

	/**
	 * Converts byte array to java data type.
	 *
	 * @param bytes
	 *            source byte array
	 * @param address
	 * @param offset
	 * @return converted java value
	 */
	T fromBytes(byte[] bytes, ParsedAddress address, int offset);

	/**
	 * Creates an array of java elements.
	 *
	 * @param count
	 *            requested size of the array.
	 * @return uninitialized array of element
	 */
	T[] createArray(int count);
}
