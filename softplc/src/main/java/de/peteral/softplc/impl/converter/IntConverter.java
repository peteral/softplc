package de.peteral.softplc.impl.converter;

import de.peteral.softplc.model.Converter;

public class IntConverter implements Converter<Short> {

	@Override
	public void toBytes(Short value, int startByte, int size, byte[] buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	public Short fromBytes(byte[] bytes, int startByte, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Short[] createArray(int count) {
		// TODO Auto-generated method stub
		return null;
	}

}
