package eisenmann.connector.plc.ra.virtualplc.telegram.s7;

import eisenmann.connector.plc.ra.virtualplc.telegram.s7.base.AbstractS7ResponseTelegramm;

public class TelWriteResponse
    extends AbstractS7ResponseTelegramm
{

    public TelWriteResponse(byte[] data)
    {
        super(data);
    }

    public TelWriteResponse()
    {
        setWriteResponse();
    }

    @Override
    public int getError()
    {
        return s7Header.getResult();
    }

    @Override
    public boolean isWrite()
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
