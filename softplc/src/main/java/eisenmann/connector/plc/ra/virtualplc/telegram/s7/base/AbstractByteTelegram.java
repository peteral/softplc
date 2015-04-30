package eisenmann.connector.plc.ra.virtualplc.telegram.s7.base;

import java.util.Arrays;

import eisenmann.connector.plc.ra.virtualplc.telegram.s7.S7;

public abstract class AbstractByteTelegram
    implements ByteTelegram
{
    protected byte[] bytes = new byte[getTelegramLen()];

    @Override
    public abstract int getTelegramLen();

    @Override
    public byte[] getBytes()
    {
        if ( bytes == null )
        {
            return new byte[0];
        }
        return bytes;
    }

    @Override
    public boolean setValues(byte[] values)
    {
        if ( (values == null) || (values.length != getTelegramLen()) )
        {
            return false;
        }
        for ( int i = 0; i < values.length; i++ )
        {
            bytes[i] = values[i];
        }
        return true;
    }

    public boolean setValues(byte[] values, int start, int len)
    {
        if ( (values == null) || (values.length < (start + len)) )
        {
            return false;
        }
        for ( int i = 0; i < bytes.length; i++ )
        {
            bytes[i] = values[i + start];
        }
        return true;
    }

    @Override
    public String getInfo()
    {
        if ( bytes == null )
        {
            return "null";
        }
        return Arrays.toString(bytes);
    }

    @Override
    public boolean isOK()
    {
        return getError() == S7.OK;
    }

    @Override
    public int getError()
    {
    	if(bytes == null || bytes.length < getTelegramLen())
    	{
    		return S7.ERR_TOO_SMALL;
    	}
    	else if(bytes.length > getTelegramLen())
    	{
    		return S7.ERR_TOO_LASRGE;
    	}
        return S7.OK;
    }


}
