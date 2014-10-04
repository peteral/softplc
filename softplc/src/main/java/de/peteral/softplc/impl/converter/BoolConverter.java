package de.peteral.softplc.impl.converter;

import de.peteral.softplc.impl.ParsedAddress;
import de.peteral.softplc.model.Converter;

public class BoolConverter implements Converter<Boolean> {

	@Override
	public Boolean[] createArray(int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toBytes(Boolean value, ParsedAddress address, byte[] buffer,
			int offset) {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean fromBytes(byte[] bytes, ParsedAddress address, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

}
