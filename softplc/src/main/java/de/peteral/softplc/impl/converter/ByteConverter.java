package de.peteral.softplc.impl.converter;

import de.peteral.softplc.model.Converter;

public class ByteConverter implements Converter<Byte> {

	@Override
	public void toBytes(Byte value, int startByte, int size, byte[] buffer) {
		buffer[startByte] = value;
	}

	@Override
	public Byte fromBytes(byte[] bytes, int startByte, int size) {
		return bytes[startByte];
	}

	@Override
	public Byte[] createArray(int count) {
		return new Byte[count];
	}

}
