package de.peteral.softplc.datatype;

import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.model.Converter;

/**
 * Converter handling 32 bit unsigned.
 *
 * @author peteral
 *
 */
public class DwordConverter implements Converter<Double> {

	@Override
	public Double[] createArray(int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toBytes(Double value, ParsedAddress address, byte[] buffer,
			int offset) {
		// TODO Auto-generated method stub

	}

	@Override
	public Double fromBytes(byte[] bytes, ParsedAddress address, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

}
