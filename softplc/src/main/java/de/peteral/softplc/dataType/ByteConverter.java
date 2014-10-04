package de.peteral.softplc.dataType;

import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.model.Converter;

/**
 * Converter implementation for byte (8 bit unsigned) conversion.
 *
 * @author peteral
 *
 */
public class ByteConverter implements Converter<Byte> {

	@Override
	public Byte[] createArray(int count) {
		return new Byte[count];
	}

	@Override
	public void toBytes(Byte value, ParsedAddress address, byte[] buffer,
			int offset) {

		buffer[offset] = value;
	}

	@Override
	public Byte fromBytes(byte[] bytes, ParsedAddress address, int offset) {
		return bytes[offset];
	}

}
