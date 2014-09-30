package de.peteral.softplc.impl.converter;

import de.peteral.softplc.model.Converter;

public class S7StringConverter implements Converter<String> {

	@Override
	public void toBytes(String value, int startByte, int size, byte[] buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	public String fromBytes(byte[] bytes, int startByte, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] createArray(int count) {
		// TODO Auto-generated method stub
		return null;
	}

}
