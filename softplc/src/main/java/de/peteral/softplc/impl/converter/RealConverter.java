package de.peteral.softplc.impl.converter;

import de.peteral.softplc.model.Converter;

public class RealConverter implements Converter<Float> {

	@Override
	public void toBytes(Float value, int startByte, int size, byte[] buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	public Float fromBytes(byte[] bytes, int startByte, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Float[] createArray(int count) {
		// TODO Auto-generated method stub
		return null;
	}

}
