package de.peteral.softplc.impl.converter;

import de.peteral.softplc.model.Converter;

public class DwordConverter implements Converter<Long> {

	@Override
	public void toBytes(Long value, int startByte, int size, byte[] buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	public Long fromBytes(byte[] bytes, int startByte, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long[] createArray(int count) {
		// TODO Auto-generated method stub
		return null;
	}

}
