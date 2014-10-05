package de.peteral.softplc.dataType;

import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.model.Converter;

/**
 * Converts 16-Bit unsigned number to Integer.
 * <p>
 * First byte is MSB.
 *
 * @author peteral
 *
 */
public class WordConverter implements Converter<Double> {

	@Override
	public Double[] createArray(int count) {
		return new Double[count];
	}

	@Override
	public void toBytes(Double value, ParsedAddress address, byte[] buffer,
			int offset) {

		buffer[offset] = (byte) (value.intValue() / 0xFF);
		buffer[offset + 1] = (byte) (value.intValue() % 0xFF);
	}

	@Override
	public Double fromBytes(byte[] bytes, ParsedAddress address, int offset) {
		return (double) (0xFF * DataTypeUtils.byteToInt(bytes[offset]))
				+ DataTypeUtils.byteToInt(bytes[offset + 1]);
	}

}
