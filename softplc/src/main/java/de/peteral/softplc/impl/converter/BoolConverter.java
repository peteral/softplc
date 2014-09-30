package de.peteral.softplc.impl.converter;

import de.peteral.softplc.model.Converter;

public class BoolConverter implements Converter<Boolean> {

	@Override
	public void toBytes(Boolean value, int startByte, int size, byte[] buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean fromBytes(byte[] bytes, int startByte, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean[] createArray(int count) {
		// TODO Auto-generated method stub
		return null;
	}

}
