package de.peteral.softplc.model;

import java.util.logging.Logger;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

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
 */
public interface MemoryArea {

	/**
	 * @return memory area code
	 */
	StringProperty getAreaCode();

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

	/**
	 * @return total size of this memory area in bytes
	 */
	IntegerProperty getSize();

	/**
	 * @return logger for this memory area
	 */
	Logger getLogger();

	/**
	 *
	 * @return default memory area is added automatically even if not configured
	 */
	boolean isDefaultArea();

	/**
	 * Resets memory area contents to zeros.
	 */
	void reset();
}
