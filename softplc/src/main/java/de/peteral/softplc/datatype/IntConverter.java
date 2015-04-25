package de.peteral.softplc.datatype;

import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.model.Converter;

/**
 * Converts signed 16-bit integer.
 *
 * @author peteral
 */
public class IntConverter
    implements Converter<Number>
{

    @Override
    public Number[] createArray(int count)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void toBytes(Number value,
                        ParsedAddress address,
                        byte[] buffer,
                        int offset)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Number fromBytes(byte[] bytes, ParsedAddress address, int offset)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
