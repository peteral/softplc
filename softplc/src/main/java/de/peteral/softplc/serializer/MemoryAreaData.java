package de.peteral.softplc.serializer;

import java.io.Serializable;

/**
 * Serializable memory area reprezentation to be saved in a memory snapshot.
 *
 * @author peteral
 *
 */
public class MemoryAreaData implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String areaCode;
	private final byte[] bytes;

	/**
	 * Default constructor
	 *
	 * @param areaCode
	 *            memory area name
	 * @param bytes
	 *            memory area content
	 */
	public MemoryAreaData(String areaCode, byte[] bytes) {
		this.areaCode = areaCode;
		this.bytes = bytes;
	}

	/**
	 * @return the areaCode
	 */
	public String getAreaCode() {
		return areaCode;
	}

	/**
	 * @return the bytes
	 */
	public byte[] getBytes() {
		return bytes;
	}

}
