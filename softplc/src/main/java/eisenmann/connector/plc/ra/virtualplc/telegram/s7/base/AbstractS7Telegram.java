package eisenmann.connector.plc.ra.virtualplc.telegram.s7.base;

import eisenmann.connector.plc.ra.virtualplc.telegram.s7.S7Telegram;
import eisenmann.connector.plc.ra.virtualplc.telegram.s7.header.HeaderRFC;
import eisenmann.connector.plc.ra.virtualplc.telegram.s7.header.HeaderS7;
import eisenmann.connector.plc.ra.virtualplc.telegram.s7.header.HeaderS7Response;
import eisenmann.connector.plc.ra.virtualplc.telegram.s7.header.S7Data;

public abstract class AbstractS7Telegram
    implements S7Telegram
{
    protected HeaderRFC rfcHeader = new HeaderRFC();
    protected HeaderS7 s7Header = new HeaderS7Response();
    protected S7Data s7data = new S7Data();

    @Override
    public int getTelegramLen()
    {
        return rfcHeader.getTelegramLen() + s7Header.getTelegramLen()
            + s7data.getTelegramLen();
    }

    @Override
    public boolean isConnect()
    {
        return false;
    }

    @Override
    public boolean isRead()
    {
        return false;
    }

    @Override
    public boolean isWrite()
    {
        return false;
    }

    @Override
    public byte[] getBytes()
    {
        byte[] bytes = new byte[getTelegramLen()];
        System.arraycopy(rfcHeader.getBytes(),
                         0,
                         bytes,
                         0,
                         rfcHeader.getTelegramLen());
        System.arraycopy(s7Header.getBytes(),
                         0,
                         bytes,
                         rfcHeader.getTelegramLen(),
                         s7Header.getTelegramLen());
        System.arraycopy(s7data.getBytes(),
                         0,
                         bytes,
                         rfcHeader.getTelegramLen() + s7Header.getTelegramLen(),
                         s7data.getTelegramLen());
        return bytes;
    }

    @Override
    public boolean setValues(byte[] values)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getInfo()
    {
        // TODO Auto-generated method stub
        return getClass() + ", Req=" + isRequest() + ", Res=" + isResponse()
            + ", Con=" + isConnect() + ", R=" + isRead() + ", W=" + isWrite();
    }

    @Override
    public boolean isOK()
    {
        // TODO Auto-generated method stub
        return getError() == 0;
    }

}
