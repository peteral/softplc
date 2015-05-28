package de.peteral.softplc.datatype;

import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.model.Converter;

/**
 * Converts signed 16-bit integer.
 *
 * @author peteral
 */
public class IntConverter implements Converter<Number> {
	private final WordConverter wordConverter = new WordConverter();

	@Override
	public Number[] createArray(int count) {
		return new Short[count];
	}

	@Override
	public void toBytes(Number value, ParsedAddress address, byte[] buffer,
			int offset) {
		wordConverter.toBytes(value, address, buffer, offset);
	}

	@Override
	public Number fromBytes(byte[] bytes, ParsedAddress address, int offset) {
		int intValue = (int) wordConverter.fromBytes(bytes, address, offset);

		return (short) intValue;
	}

	@Override
	public void parseToBytes(String value, ParsedAddress address,
			byte[] buffer, int offset) {

		toBytes(Short.parseShort(value), address, buffer, offset);
	}
}
