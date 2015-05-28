package de.peteral.softplc.datatype;

import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.model.Converter;

/**
 * Converts S7 string to / from byte arrays.
 * <p>
 * Structure:
 * <ul>
 * <li><b>+0</b> - total size
 * <li><b>+1</b> - actual string length
 * <li><b>+2</b> - start of character data
 * </ul>
 *
 * @author peteral
 *
 */
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

	@Override
	public void parseToBytes(String value, ParsedAddress address,
			byte[] buffer, int offset) {
		// TODO Auto-generated method stub

	}

}
