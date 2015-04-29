package eisenmann.connector.plc.ra.virtualplc.telegram.s7;

import eisenmann.connector.plc.ra.virtualplc.telegram.s7.base.AbstractS7RequestTelegramm;

public class TelWriteRequest
    extends AbstractS7RequestTelegramm
{
    public TelWriteRequest(byte[] data)
    {
        super(data);
    }

    public TelWriteRequest(int count, int db, int offset, byte[] byteData)
    {
        s7data.setWriteRequest(count, db, offset, byteData, true, 0);
        rfcHeader.setLenOfS7Telegramm(getTelegramLen());
    }

    public TelWriteRequest(int count, int db, int offset, int bitNum, byte[] byteData)
    {
        s7data.setWriteRequest(count, db, offset, byteData, false, bitNum);
        rfcHeader.setLenOfS7Telegramm(getTelegramLen());
    }

   @Override
    public TelWriteResponse getResponse()
    {
        TelWriteResponse response = new TelWriteResponse();
        return response;
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

    public byte[] getWrtieData()
    {
        return s7data.getWriteData();
    }

    public boolean getBitValue()
    {
    	return s7data.getBitValue();
    }
    
    public int getBitNumber()
    {
    	return s7data.getBitNumber();
    }

    @Override
    public int getError()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isWrite()
    {
        return true;
    }
    
    @Override
	public boolean isByteType()
    {
    	return s7data.isByteType();    	 
    }
    
    @Override
	public boolean isBitType()
    {
    	return s7data.isBitType();    	 
    }
}
