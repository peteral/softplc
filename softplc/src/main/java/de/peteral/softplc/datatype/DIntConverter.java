package de.peteral.softplc.datatype;

import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.model.Converter;

/**
 * Converter handling 32 bit signed decimals.
 *
 * @author peteral
 */
public class DIntConverter
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
