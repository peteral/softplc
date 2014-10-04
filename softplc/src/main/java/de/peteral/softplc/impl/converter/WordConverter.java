package de.peteral.softplc.impl.converter;

import de.peteral.softplc.impl.address.ParsedAddress;
import de.peteral.softplc.model.Converter;

/**
 * Converts 16-Bit unsigned number to Integer.
 * <p>
 * First byte is MSB.
 *
 * @author peteral
 *
 */
public class WordConverter implements Converter<Integer> {

	@Override
	public Integer[] createArray(int count) {
		return new Integer[count];
	}

	@Override
	public void toBytes(Integer value, ParsedAddress address, byte[] buffer,
			int offset) {

		buffer[offset] = (byte) (value / 0xFF);
		buffer[offset + 1] = (byte) (value % 0xFF);
	}

	@Override
	public Integer fromBytes(byte[] bytes, ParsedAddress address, int offset) {
		return (0xFF * bytes[offset]) + bytes[offset + 1];
	}

}
