package de.peteral.softplc.dataType;

import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.model.Converter;

/**
 * Converter implementation for byte (8 bit unsigned) conversion.
 *
 * @author peteral
 *
 */
public class ByteConverter implements Converter<Double> {

	@Override
	public Double[] createArray(int count) {
		return new Double[count];
	}

	@Override
	public void toBytes(Double value, ParsedAddress address, byte[] buffer,
			int offset) {

		buffer[offset] = value.byteValue();
	}

	@Override
	public Double fromBytes(byte[] bytes, ParsedAddress address, int offset) {
		return Double.valueOf(DataTypeUtils.byteToInt(bytes[offset]));
	}

}
