package eisenmann.connector.plc.ra.virtualplc.telegram.s7;

import eisenmann.connector.plc.ra.virtualplc.telegram.s7.base.AbstractS7ResponseTelegramm;

public class TelConnectResponse
    extends AbstractS7ResponseTelegramm
{
    public TelConnectResponse(byte[] data)
    {
        super(data);
    }

    public TelConnectResponse()
    {
        setConnectResponse();
    }

    private static int LEN = 5;
    private static final Byte RESPONSE_OK = (byte) 0xD0;

    @Override
    public int getTelegramLen()
    {
        return super.getTelegramLen();
    }

    @Override
    public String getInfo()
    {
        return getState() + " " + super.getInfo();
    }

    public String getState()
    {
        if ( (getBytes().length < 5) )
        {
            return "No info (" + getBytes().length + ")";
        }
        Byte stateByte = getBytes()[4];
        return (isOK() ? "OK" : "ERROR " + stateByte);
    }

    @Override
    public int getError()
    {
        if ( (getBytes().length < 5) )
        {
            return -1;
        }
        Byte stateByte = getBytes()[4];
        return ((RESPONSE_OK.equals(stateByte)) ? 0 : -2);
    }

    @Override
    public boolean isConnect()
    {
        return true;
    }
    
    @Override
	public boolean isByteType()
    {
    	return false;    	 
    	     }
    
    @Override
	public boolean isBitType()
    {
    	return false;    	 
    }

}
