package de.peteral.softplc.datatype;

import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.model.Converter;

/**
 * Converter handling 32 bit unsigned.
 *
 * @author peteral
 */
public class DwordConverter
    implements Converter<Number>
{

    @Override
    public Number[] createArray(int count)
    {
        return new Integer[count];
    }

    @Override
    public void toBytes(Number value,
                        ParsedAddress address,
                        byte[] buffer,
                        int offset)
    {
        buffer[offset + 3] = (byte) (value.longValue() & 0xFF);
        buffer[offset + 2] = (byte) ((value.longValue() & 0xFF00) >> 8);
        buffer[offset + 1] = (byte) ((value.longValue() & 0xFF0000) >> 16);
        buffer[offset] = (byte) ((value.longValue() & 0xFF000000) >> 24);
    }

    @Override
    public Number fromBytes(byte[] bytes, ParsedAddress address, int offset)
    {
        return (long) (DataTypeUtils.byteToInt(bytes[offset]) << 24)
            + (long) (DataTypeUtils.byteToInt(bytes[offset + 1]) << 16)
            + (DataTypeUtils.byteToInt(bytes[offset + 2]) << 8)
            + DataTypeUtils.byteToInt(bytes[offset + 3]);
    }

}
