package de.peteral.softplc.memory;

import java.util.Arrays;
import java.util.logging.Logger;

import de.peteral.softplc.model.MemoryAccessViolationException;
import de.peteral.softplc.model.MemoryArea;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * {@link MemoryArea} implementation.
 *
 * @author peteral
 */
public class MemoryAreaImpl implements MemoryArea {

	private final StringProperty areaCode;
	private final IntegerProperty size;
	private byte[] buffer;
	private transient final Logger logger;
	private final boolean defaultArea;

	/**
	 * Creates a new instance.
	 *
	 * @param areaCode
	 *            identifier of this {@link MemoryArea}
	 * @param size
	 *            total size in bytes
	 * @param defaultArea
	 *            true - automatically added memory area, bypass during save
	 */
	public MemoryAreaImpl(String areaCode, int size, boolean defaultArea) {
		this.defaultArea = defaultArea;
		this.areaCode = new SimpleStringProperty(areaCode);
		this.size = new SimpleIntegerProperty(size);
		createBuffer();
		logger = Logger.getLogger("memory." + areaCode);

		this.size.addListener(event -> createBuffer());
	}

	private byte[] createBuffer() {
		return buffer = new byte[this.size.get()];
	}

	@Override
	public StringProperty getAreaCode() {
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
			throw new MemoryAccessViolationException("Invalid " + method + " access areaCode = " + areaCode.get()
					+ ", offset = " + offset + ", len = " + size);
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

	@Override
	public IntegerProperty getSize() {
		return size;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public boolean isDefaultArea() {
		return defaultArea;
	}
}
