package de.peteral.softplc.impl.converter;

import de.peteral.softplc.impl.ParsedAddress;
import de.peteral.softplc.model.Converter;

public class WordConverter implements Converter<Integer> {

	@Override
	public Integer[] createArray(int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toBytes(Integer value, ParsedAddress address, byte[] buffer,
			int offset) {
		// TODO Auto-generated method stub

	}

	@Override
	public Integer fromBytes(byte[] bytes, ParsedAddress address, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

}
