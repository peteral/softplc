package eisenmann.connector.plc.ra.virtualplc.telegram.s7.base;

import eisenmann.connector.plc.ra.virtualplc.telegram.s7.S7Telegram;
import eisenmann.connector.plc.ra.virtualplc.telegram.s7.header.HeaderS7Request;

public abstract class AbstractS7RequestTelegramm
    extends AbstractS7Telegram
    implements S7Telegram

{

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

    public abstract AbstractS7ResponseTelegramm getResponse();

    public int getS7DataCount()
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


}
