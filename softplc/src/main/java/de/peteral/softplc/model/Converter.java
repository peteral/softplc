package de.peteral.softplc.model;

/**
 * Classes implementing this interface are responsible for converting of java
 * types to byte arrays and vice versa.
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
	 * @param startByte
	 *            start offset within the byte buffer
	 * @param size
	 *            size of the element in bytes
	 * @param buffer
	 *            byte buffer
	 */
	void toBytes(T value, int startByte, int size, byte[] buffer);

	/**
	 * Converts byte array to java data type.
	 *
	 * @param bytes
	 *            source byte array
	 * @param startByte
	 *            start offset within the source byte array
	 * @param size
	 *            size of the element in bytes
	 * @return converted java value
	 */
	T fromBytes(byte[] bytes, int startByte, int size);

	/**
	 * Creates an array of java elements.
	 *
	 * @param count
	 *            requested size of the array.
	 * @return uninitialized array of element
	 */
	T[] createArray(int count);
}
