package de.peteral.softplc.datatype;

import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.model.Converter;

/**
 * Converter implementation for byte (8 bit unsigned) conversion.
 *
 * @author peteral
 */
public class ByteConverter implements Converter<Number> {

	@Override
	public Number[] createArray(int count) {
		return new Integer[count];
	}

	@Override
	public void toBytes(Number value, ParsedAddress address, byte[] buffer,
			int offset) {

		buffer[offset] = value.byteValue();
	}

	@Override
	public Number fromBytes(byte[] bytes, ParsedAddress address, int offset) {
		return Integer.valueOf(DataTypeUtils.byteToInt(bytes[offset]));
	}

	@Override
	public void parseToBytes(String value, ParsedAddress address,
			byte[] buffer, int offset) {

		toBytes(Byte.parseByte(value), address, buffer, offset);
	}

}
