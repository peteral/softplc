package de.peteral.softplc.datatype;

import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.model.Converter;

/**
 * Converts string stored in an character array to / from a java String.
 * <p>
 *
 * @author peteral
 *
 */
public class StringConverter implements Converter<String> {

	@Override
	public String[] createArray(int count) {
		return new String[count];
	}

	@Override
	public void toBytes(String value, ParsedAddress address, byte[] buffer,
			int offset) {
		assertValidBuffer(buffer, address, offset);

		byte[] bytes = value.getBytes();
		for (int i = 0; i < value.length(); i++) {
			buffer[offset + i] = bytes[i];
		}

		if (value.length() < address.getSize()) {
			buffer[offset + value.length()] = 0;
		}
	}

	private void assertValidBuffer(byte[] buffer, ParsedAddress address,
			int offset) {
		if (buffer.length < (address.getSize() + offset)) {
			throw new ConverterException("Invalid buffer length ["
					+ buffer.length + "] for address [" + address + "]!");
		}
	}

	@Override
	public String fromBytes(byte[] bytes, ParsedAddress address, int offset) {
		int length = Math.min(address.getSize(), bytes.length - offset);

		for (int i = offset; i < (offset + length); i++) {
			if (bytes[i] == 0) {
				length = (i - offset);
			}
		}

		return new String(bytes, offset, length);
	}

	@Override
	public void parseToBytes(String value, ParsedAddress address,
			byte[] buffer, int offset) {

		toBytes(value, address, buffer, offset);
	}
}
