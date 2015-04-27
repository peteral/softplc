package eisenmann.connector.plc.ra.virtualplc.telegram.s7;

import eisenmann.connector.plc.ra.virtualplc.telegram.s7.base.AbstractS7RequestTelegramm;

public class S7TelegrammFactory
{
    private static S7TelegrammFactory instance = new S7TelegrammFactory();

    private S7TelegrammFactory()
    {
    }

    public static S7TelegrammFactory get()
    {
        if ( instance == null )
        {
            instance = new S7TelegrammFactory();
        }
        return instance;
    }

    public S7Telegram newTelegam(byte[] bytes)
    {
        if ( (bytes == null) || (bytes.length < 22) )
        {
            return null;
        }
        if ( S7.TEL_RESPONSE == bytes[S7.POS_TYPE_RR] )
        {
            if ( S7.CMD_CON == bytes[S7.POS_TYPE_REQEST_RW] 
            		)//|| S7.CMD_CON2== bytes[S7.POS_TYPE_REQEST_RW] )
            {
                //return new TelConnectResponse(bytes);
            }
            else if ( S7.CMD_WRITE == bytes[S7.POS_TYPE_REQEST_RW] )
            {
                return new TelWriteResponse(bytes);
            }
            else if ( S7.CMD_READ == bytes[S7.POS_TYPE_REQEST_RW] )
            {
                return new TelReadResponse(bytes);
            }
            return null;

        }
        else if ( S7.TEL_REQUEST == bytes[S7.POS_TYPE_RR] )
        {
            if ( S7.CMD_CON == bytes[S7.POS_TYPE_REQEST_RW] 
            		)//|| S7.CMD_CON2== bytes[S7.POS_TYPE_REQEST_RW] )
            {
                return new TelConnectRequest(bytes);
            }
            else if ( S7.CMD_WRITE == bytes[S7.POS_TYPE_REQEST_RW] )
            {
                return new TelWriteRequest(bytes);
            }
            else if ( S7.CMD_READ == bytes[S7.POS_TYPE_REQEST_RW] )
            {
                return new TelReadRequest(bytes);
            }
            return null;

        }

        return null;
    }

    public AbstractS7RequestTelegramm newConnect(int slot)
    {
        return new TelConnectRequest(slot);
    }

    public TelReadRequest newRead(int count, int db, int offset)
    {
        return new TelReadRequest(count, db, offset);
    }

    public TelReadResponse newReadResponse(int count,
                                           int db,
                                           int offset,
                                           byte[] data)
    {
        return new TelReadResponse(count, db, offset, data);
    }

    public TelReadResponse newReadResponse(int count,
                                           String db,
                                           int offset,
                                           byte[] data)
    {
        return new TelReadResponse(count, db, offset, data);
    }

    public TelWriteRequest newWrite(int count,
                                    int db,
                                    int offset,
                                    byte[] byteData)
    {
        return new TelWriteRequest(count, db, offset, byteData);
    }

    public TelWriteResponse newWriteResponse()
    {
        //TODO is there any bytes for data-defintion //int count, String db, int offset)
        return new TelWriteResponse();
    }

}
