package eisenmann.connector.plc.ra.virtualplc.telegram.s7;

import eisenmann.connector.plc.ra.virtualplc.telegram.s7.base.AbstractS7RequestTelegramm;

public class TelReadRequest
    extends AbstractS7RequestTelegramm
{

    public TelReadRequest(byte[] data)
    {
        super(data);
    }

    public TelReadRequest(int count, int db, int offset)
    {
        s7data.setReadRequest(count, db, offset);
    }

    @Override
    public TelReadResponse getResponse()
    {
        TelReadResponse response = new TelReadResponse(getS7DataCount());

        //response.setValues(new byte[getCountDef()]);
        return response;
    }

    @Override
    public int getError()
    {
        return 0;
    }

    @Override
    public boolean isRead()
    {
        return true;
    }

    public int getDataByteCount()
    {
        return s7data.getReadData().length;
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
