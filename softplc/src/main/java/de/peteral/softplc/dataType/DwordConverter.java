package de.peteral.softplc.dataType;

import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.model.Converter;

public class DwordConverter implements Converter<Long> {

	@Override
	public Long[] createArray(int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toBytes(Long value, ParsedAddress address, byte[] buffer,
			int offset) {
		// TODO Auto-generated method stub

	}

	@Override
	public Long fromBytes(byte[] bytes, ParsedAddress address, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

}
