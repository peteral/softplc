package de.peteral.softplc.model;

/**
 * Represents a memory area in the {@link Cpu}.
 * <p>
 * Following memory areas are available:
 * <ul>
 * <li>M
 * <li>I
 * <li>O
 * <li>T
 * <li>C
 * <li>DBnnn
 * </ul>
 *
 * @author peteral
 *
 */
public interface MemoryArea {

	/**
	 *
	 * @return memory area code
	 */
	String getAreaCode();

	/**
	 * Reads data from the memory.
	 *
	 * @param offset
	 *            start reading from this offset
	 * @param length
	 *            number of bytes
	 * @return byte array containing data from requested memory area
	 * @throws MemoryAccessViolationException
	 *             for invalid parameters
	 */
	byte[] readBytes(int offset, int length);

	/**
	 * Writes data to memory.
	 *
	 * @param offset
	 *            offset of the first byte to be written to
	 * @param data
	 *            data to be written (length according the length of the data
	 *            array)
	 * @throws MemoryAccessViolationException
	 *             for invalid parameters
	 */
	void writeBytes(int offset, byte[] data);

	/**
	 * Sets a bit in memory.
	 *
	 * @param offset
	 *            byte offset
	 * @param bitNumber
	 *            bit number
	 * @param value
	 *            new value
	 * @throws MemoryAccessViolationException
	 *             for invalid parameters
	 */
	void setBit(int offset, int bitNumber, boolean value);
}
