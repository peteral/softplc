package de.peteral.softplc.datatype;

import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.model.Converter;

/**
 * Converter handling 32 bit float.
 *
 * @author peteral
 */
public class RealConverter implements Converter<Number> {
	private final DwordConverter dwordConverter = new DwordConverter();

	@Override
	public Number[] createArray(int count) {
		return new Float[count];
	}

	@Override
	public void toBytes(Number value, ParsedAddress address, byte[] buffer,
			int offset) {
		dwordConverter.toBytes(Float.floatToIntBits(value.floatValue()),
				address, buffer, offset);
	}

	@Override
	public Number fromBytes(byte[] bytes, ParsedAddress address, int offset) {
		Number dw = dwordConverter.fromBytes(bytes, address, offset);

		return Float.intBitsToFloat((int) dw.longValue());
	}

	@Override
	public void parseToBytes(String value, ParsedAddress address,
			byte[] buffer, int offset) {

		toBytes(Float.parseFloat(value), address, buffer, offset);
	}
}
