package de.peteral.softplc.impl.converter;

import de.peteral.softplc.model.Converter;

public class StringConverter implements Converter<String> {

	@Override
	public void toBytes(String value, int startByte, int size, byte[] buffer) {
		for (int i = 0; i < value.length(); i++) {
			buffer[startByte + i] = (byte) value.charAt(i);
		}
	}

	@Override
	public String fromBytes(byte[] bytes, int startByte, int size) {

		return null;
	}

	@Override
	public String[] createArray(int count) {
		// TODO Auto-generated method stub
		return null;
	}

}
