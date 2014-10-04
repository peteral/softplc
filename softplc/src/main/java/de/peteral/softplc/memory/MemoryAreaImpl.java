package de.peteral.softplc.memory;

import java.util.Arrays;

import de.peteral.softplc.model.MemoryAccessViolationException;
import de.peteral.softplc.model.MemoryArea;

/**
 * {@link MemoryArea} implementation.
 *
 * @author peteral
 *
 */
public class MemoryAreaImpl implements MemoryArea {

	private final String areaCode;
	private final byte[] buffer;

	/**
	 * Creates a new instance.
	 *
	 * @param areaCode
	 *            identifier of this {@link MemoryArea}
	 * @param size
	 *            total size in bytes
	 */
	public MemoryAreaImpl(String areaCode, int size) {
		this.areaCode = areaCode;
		buffer = new byte[size];
	}

	@Override
	public String getAreaCode() {
		return areaCode;
	}

	@Override
	public byte[] readBytes(int offset, int length) {
		checkValid(offset, length, "read");

		return Arrays.copyOfRange(buffer, offset, offset + length);
	}

	@Override
	public void writeBytes(int offset, byte[] data) {
		checkValid(offset, data.length, "write");

		for (int i = 0; i < data.length; i++) {
			buffer[offset + i] = data[i];
		}
	}

	private void checkValid(int offset, int size, String method) {
		if ((offset < 0) || ((offset + size) > buffer.length)) {
			throw new MemoryAccessViolationException("Invalid " + method
					+ " access areaCode = " + areaCode + ", offset = " + offset
					+ ", len = " + size);
		}
	}

	@Override
	public void setBit(int offset, int bitNumber, boolean value) {
		checkValid(offset, 1, "set bit");

		if (value) {
			buffer[offset] = (byte) (buffer[offset] | (1 << bitNumber));
		} else {
			buffer[offset] = (byte) (buffer[offset] & (0xFF - (1 << bitNumber)));
		}
	}
}
