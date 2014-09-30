package de.peteral.softplc.impl.converter;

import java.util.Date;

import de.peteral.softplc.model.Converter;

public class DateConverter implements Converter<Date> {

	@Override
	public void toBytes(Date value, int startByte, int size, byte[] buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	public Date fromBytes(byte[] bytes, int startByte, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date[] createArray(int count) {
		// TODO Auto-generated method stub
		return null;
	}

}
