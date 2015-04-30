package eisenmann.connector.plc.ra.virtualplc.telegram.s7.base;

import eisenmann.connector.plc.ra.virtualplc.telegram.s7.S7Telegram;

public abstract class AbstractS7ResponseTelegramm
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
    public AbstractS7ResponseTelegramm(byte[] data)
    {
        rfcHeader.setValues(data, 0, rfcHeader.getTelegramLen());
        s7Header.setValues(data,
                           rfcHeader.getTelegramLen(),
                           s7Header.getTelegramLen());
        int offset = rfcHeader.getTelegramLen() + s7Header.getTelegramLen();
        s7data.setValues(data, offset, data.length + offset);
    }

    public AbstractS7ResponseTelegramm()
    {

    }

    public void setConnectResponse()
    {
        s7data.setConnectResponse();
    }

    public void setWriteResponse()
    {
        s7data.setWriteResponse();
    }


}
