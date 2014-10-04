package de.peteral.softplc.model;

import de.peteral.softplc.impl.ParsedAddress;

/**
 * This interface provides access to the memory of a {@link Cpu}.
 * <p>
 *
 * @author peteral
 *
 */
public interface Memory {
	/**
	 * Look up a {@link MemoryArea} by it's area code.
	 * <p>
	 *
	 * @param areaCode
	 *            area code, examples: "M", "DB100", "I"
	 * @return {@link MemoryArea} instance associated with given areaCode
	 * @throws MemoryAccessViolationException
	 *             in case of an invalid memory area code
	 */
	MemoryArea getMemoryArea(String areaCode);

	/**
	 * Read bytes from memory.
	 * <p>
	 *
	 * @param area
	 *            area code, examples: "M", "DB100", "I"
	 * @param offset
	 *            first byte offset
	 * @param length
	 *            number of bytes to be read
	 * @return byte array containing data from requested memory location
	 */
	byte[] readBytes(String area, int offset, int length);

	/**
	 * Writes bytes to memory.
	 * <p>
	 *
	 * @param area
	 *            area code, examples: "M", "DB100", "I"
	 * @param offset
	 *            first byte offset
	 * @param data
	 *            bytes to be written
	 */
	void writeBytes(String area, int offset, byte[] data);

	/**
	 * Reads from memory and converts the result to the data type according the
	 * address string.
	 * <p>
	 *
	 * @param address
	 *            address string
	 * @return result according type encoded within address
	 * @see ParsedAddress
	 */
	Object read(String address);

	/**
	 * Writes a value to memory.
	 * <p>
	 * The data is converted according the address.
	 *
	 * @param address
	 *            defines the location to write and the data type
	 * @param value
	 *            value to be written
	 * @see ParsedAddress
	 */
	void write(String address, Object value);

}
