package de.peteral.softplc.dataType;

import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.model.Converter;

public class IntConverter implements Converter<Short> {

	@Override
	public Short[] createArray(int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toBytes(Short value, ParsedAddress address, byte[] buffer,
			int offset) {
		// TODO Auto-generated method stub

	}

	@Override
	public Short fromBytes(byte[] bytes, ParsedAddress address, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

}
