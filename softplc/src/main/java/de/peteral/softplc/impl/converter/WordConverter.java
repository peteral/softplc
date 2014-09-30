package de.peteral.softplc.impl.converter;

import de.peteral.softplc.model.Converter;

public class WordConverter implements Converter<Integer> {

	@Override
	public void toBytes(Integer value, int startByte, int size, byte[] buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	public Integer fromBytes(byte[] bytes, int startByte, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer[] createArray(int count) {
		// TODO Auto-generated method stub
		return null;
	}

}
