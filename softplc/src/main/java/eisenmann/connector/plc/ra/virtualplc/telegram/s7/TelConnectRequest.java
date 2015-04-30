package eisenmann.connector.plc.ra.virtualplc.telegram.s7;

import eisenmann.connector.plc.ra.virtualplc.telegram.s7.base.AbstractS7RequestTelegramm;
import eisenmann.connector.plc.ra.virtualplc.telegram.s7.base.AbstractS7ResponseTelegramm;

public class TelConnectRequest
    extends AbstractS7RequestTelegramm//AbstractByteTelegram
{
    public TelConnectRequest(byte[] data)
    {
        super(data);
    }

    //private static final int OFFSET_RACK_AND_SLOT = 18;

    @Override
    public int getError()
    {
        if ( s7data.getSlot() > 0 ) //(bytes[OFFSET_RACK_AND_SLOT] & 0x1F) > 0 )
        {
            return 0;
        }
        return -1;
    }

    public TelConnectRequest(int slot)
    {
        setConnectRequest(slot);
    }

    @Override
    public boolean isConnect()
    {
        return true;
    }

    @Override
    public AbstractS7ResponseTelegramm getResponse()
    {
        TelConnectResponse response = new TelConnectResponse();
        return response;
    }

    public int getSlot()
    {
    	return s7data.getSlot();
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
