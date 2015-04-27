package de.peteral.softplc.datatype;

import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.model.Converter;

/**
 * Converts 16-Bit unsigned number to Integer.
 * <p>
 * First byte is MSB.
 *
 * @author peteral
 */
public class WordConverter
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

        buffer[offset + 1] = (byte) (value.intValue() & 0xFF);
        buffer[offset + 0] = (byte) ((value.intValue() & 0xFF00) >> 8);
    }

    @Override
    public Number fromBytes(byte[] bytes, ParsedAddress address, int offset)
    {
        return (DataTypeUtils.byteToInt(bytes[offset]) << 8)
            + DataTypeUtils.byteToInt(bytes[offset + 1]);
    }

}
