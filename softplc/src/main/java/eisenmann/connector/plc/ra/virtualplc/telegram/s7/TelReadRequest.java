package eisenmann.connector.plc.ra.virtualplc.telegram.s7;

import eisenmann.connector.plc.ra.virtualplc.telegram.s7.base.AbstractS7RequestTelegramm;

public class TelReadRequest
    extends AbstractS7RequestTelegramm
{
    //Short count;

    public TelReadRequest(byte[] data)
    {
        super(data);
    }

    public TelReadRequest(int count, int db, int offset)
    {
        //  this.count = count;
        s7data.setReadRequest(count, db, offset);
    }

    @Override
    public TelReadResponse getResponse()
    {
        TelReadResponse response = new TelReadResponse(getCountDef());

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

    public int getCount()
    {
        return s7data.getReadData().length;
    }

    public int getCountDef()
    {
        return s7data.getCount();
    }

    public int getDbNum()
    {
        return s7data.getDbNum();
    }

    public int getOffset()
    {
        return s7data.getOffset();
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
