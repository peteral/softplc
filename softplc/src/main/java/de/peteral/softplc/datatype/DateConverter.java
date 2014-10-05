package de.peteral.softplc.datatype;

import java.util.Date;

import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.model.Converter;

/**
 * Converts S7 DATE_AND_TIME from / to byte arrays.
 *
 * @author peteral
 *
 */
public class DateConverter implements Converter<Date> {

	@Override
	public Date[] createArray(int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toBytes(Date value, ParsedAddress address, byte[] buffer,
			int offset) {
		// TODO Auto-generated method stub

	}

	@Override
	public Date fromBytes(byte[] bytes, ParsedAddress address, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

}
