package de.peteral.softplc.datatype;

import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.model.Converter;

/**
 * Converter handling 32 bit signed decimals.
 *
 * @author peteral
 */
public class DIntConverter implements Converter<Number> {
	private final DwordConverter dwordConverter = new DwordConverter();

	@Override
	public Number[] createArray(int count) {
		return new Integer[count];
	}

	@Override
	public void toBytes(Number value, ParsedAddress address, byte[] buffer,
			int offset) {
		dwordConverter.toBytes(value, address, buffer, offset);
	}

	@Override
	public Number fromBytes(byte[] bytes, ParsedAddress address, int offset) {
		long longValue = (long) dwordConverter
				.fromBytes(bytes, address, offset);
		return (int) longValue;
	}

	@Override
	public void parseToBytes(String value, ParsedAddress address,
			byte[] buffer, int offset) {

		toBytes(Long.parseLong(value), address, buffer, offset);
	}

}
