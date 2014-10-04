package de.peteral.softplc.impl.converter;

import de.peteral.softplc.impl.ParsedAddress;
import de.peteral.softplc.model.Converter;

public class ByteConverter implements Converter<Byte> {

	@Override
	public Byte[] createArray(int count) {
		return new Byte[count];
	}

	@Override
	public void toBytes(Byte value, ParsedAddress address, byte[] buffer,
			int offset) {
		// TODO Auto-generated method stub

	}

	@Override
	public Byte fromBytes(byte[] bytes, ParsedAddress address, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

}
