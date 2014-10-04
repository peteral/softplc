package de.peteral.softplc.dataType;

import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.model.Converter;

public class RealConverter implements Converter<Float> {

	@Override
	public Float[] createArray(int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toBytes(Float value, ParsedAddress address, byte[] buffer,
			int offset) {
		// TODO Auto-generated method stub

	}

	@Override
	public Float fromBytes(byte[] bytes, ParsedAddress address, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

}
