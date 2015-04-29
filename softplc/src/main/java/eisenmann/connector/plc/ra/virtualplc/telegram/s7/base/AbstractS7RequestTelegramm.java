package eisenmann.connector.plc.ra.virtualplc.telegram.s7.base;

import eisenmann.connector.plc.ra.virtualplc.telegram.s7.S7Telegram;
import eisenmann.connector.plc.ra.virtualplc.telegram.s7.header.HeaderS7Request;

public abstract class AbstractS7RequestTelegramm
    extends AbstractS7Telegram
    implements S7Telegram

{

//    private final byte version = 0x03;
//    private static final int POS_VER = 0;
//    private final byte res1 = 0x00;
//    private static final int POS_LEN = 0;
//    //len-value get by getTelegramLen()
//    private static final int POS_ = 0;
//    private static final int POS_ = 0;

    /**
     * Create copy of telegram by its byte-stream
     * <p>
     * <p>
     * 
     * @param data
     */
    public AbstractS7RequestTelegramm(byte[] data)
    {
        s7Header = new HeaderS7Request();
        rfcHeader.setValues(data, 0, rfcHeader.getTelegramLen());
        s7Header.setValues(data,
                           rfcHeader.getTelegramLen(),
                           s7Header.getTelegramLen());
        int offset = rfcHeader.getTelegramLen() + s7Header.getTelegramLen();
        s7data.setValues(data, offset, data.length - offset);
    }

    public AbstractS7RequestTelegramm(int count,
                                      int db,
                                      int offset,
                                      byte[] byteData)
    {
        s7Header = new HeaderS7Request();
    }

    public AbstractS7RequestTelegramm()
    {
        s7Header = new HeaderS7Request();
    }

    public void setConnectRequest(int slot)
    {
        s7Header = new HeaderS7Request();
        s7data.setConnectRequest(slot);
    }

    public void setReadRequest(int count, int db, int offset)
    {
        s7data.setReadRequest(count, db, offset);
    }

//    public byte[] getReadData()
//    {
//        return Arrays.copyOfRange(bytes, POS_DATA_READ, bytes.length
//            + POS_DATA_READ);
//    }

//    public void setWriteRequest(int count, int db, int offset, byte[] byteData)
//    {
//        s7data.setWriteRequest(count, db, offset, byteData);
//    }

    public abstract AbstractS7ResponseTelegramm getResponse();

    @Override
    public boolean isRequest()
    {
        return s7Header.isRequest();
    }

    @Override
    public boolean isResponse()
    {
        return s7Header.isResponse();
    }

}
