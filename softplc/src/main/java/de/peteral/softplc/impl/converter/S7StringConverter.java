package de.peteral.softplc.impl.converter;

import de.peteral.softplc.impl.ParsedAddress;
import de.peteral.softplc.model.Converter;

public class S7StringConverter implements Converter<String> {

	@Override
	public String[] createArray(int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toBytes(String value, ParsedAddress address, byte[] buffer,
			int offset) {
		// TODO Auto-generated method stub

	}

	@Override
	public String fromBytes(byte[] bytes, ParsedAddress address, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

}
