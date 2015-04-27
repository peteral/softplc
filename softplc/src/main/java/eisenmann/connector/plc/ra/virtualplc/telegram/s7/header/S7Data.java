package eisenmann.connector.plc.ra.virtualplc.telegram.s7.header;

import java.util.Arrays;

import eisenmann.connector.plc.ra.virtualplc.telegram.s7.S7Bytes;
import eisenmann.connector.plc.ra.virtualplc.telegram.s7.base.AbstractByteTelegram;

public class S7Data
    extends AbstractByteTelegram
{
    private static final int POS_DATA_READ = 6;
    private static final int POS_DATA_WRITE = 18;
    private static final int POS_SLOT = 3;

    public S7Data()
    {
        if ( bytes == null )
        {
            bytes = new byte[0];
        }
    }

    @Override
    public boolean setValues(byte[] values, int start, int len)
    {
        bytes = new byte[len];
        super.setValues(values, start, len);
        //System.arraycopy(values, start, bytes, 0, len);
        return true;
    }

    @Override
    public int getError()
    {
        return ((bytes == null) || (bytes.length < 3)) ? -1 : 0;
    }

    @Override
    public int getTelegramLen()
    {
        if ( bytes == null )
        {
            return 0;
        }
        return bytes.length;
    }

    public void setConnectRequest(int slot)
    {
        byte[] tmp = { 0x00, 0x00, 0x00, 0x01, 0x07, (byte) 0x80 };
        bytes = tmp;
        bytes[POS_SLOT] = (byte) slot;
        S7Bytes.setConnectId(bytes);
    }

    public void setConnectResponse()
    {
        byte[] tmp = { 0x00, 0x00, 0x00, 0x01, 0x00, 0x01, 0x01, (byte) 0xe0 };
        bytes = tmp;
        S7Bytes.setConnectId(bytes);
    }

    private static final int POS_COUNT = 6;
    private static final int POS_DB = 8;
    private static final int POS_OFFSET = 12;//???

    public void setReadRequest(int count, int db, int offset)
    {
        byte[] tmp =
            { 0x00, 0x01, 0x12, 0x0a, 0x10, 0x02, (byte) 0xcc, (byte) 0xcc,
                (byte) 0xdb, (byte) 0xdb, (byte) 0x84, 0x00, 0x00, 0x00 };
        bytes = new byte[tmp.length];
        System.arraycopy(tmp, 0, bytes, 0, tmp.length);
        S7Bytes.setShort(bytes, count, POS_COUNT);
        S7Bytes.setShort(bytes, db, POS_DB);
        S7Bytes.setShort(bytes, offset, POS_OFFSET);
        //TODO set data-len.which byte??
        S7Bytes.setReadId(bytes);
    }

//    public void setReadResponse(byte[] byteData)
//    {
//        byte[] tmp = { 0x00, 0x01, (byte) 0xff, 0x04, 0x00, 0x00 };//TODO last 2 byte len
//        bytes = new byte[tmp.length + byteData.length];
//        System.arraycopy(tmp, 0, bytes, 0, tmp.length);
//        System.arraycopy(tmp, 0, bytes, POS_DATA_READ, tmp.length);
//        setReadId();
//    }
//
//    public void setReadResponse(int len)
//    {
//        setReadResponse(new byte[len]);
//    }

    public byte[] getReadData()
    {
        return Arrays.copyOfRange(bytes, POS_DATA_READ, bytes.length
            - POS_DATA_READ);
    }

    public void setWriteRequest(int count, int db, int offset, byte[] byteData)
    {
        byte[] tmp =
            { 0x00, 0x01, 0x12, 0x0a, 0x10, 0x02, (byte) 0xcc, (byte) 0xcc,
                (byte) 0xdb, (byte) 0xdb, (byte) 0x84, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00 };
        bytes = new byte[tmp.length + byteData.length];
        System.arraycopy(tmp, 0, bytes, 0, tmp.length);
        S7Bytes.setShort(bytes, count, POS_COUNT);
        S7Bytes.setShort(bytes, db, POS_DB);
        S7Bytes.setShort(bytes, offset, POS_OFFSET);
        //TODO set data-len.which byte??
        System.arraycopy(byteData, 0, bytes, POS_DATA_WRITE, byteData.length);
        S7Bytes.setWriteId(bytes);
    }

    public byte[] getWriteData()
    {
        return Arrays.copyOfRange(bytes, POS_DATA_WRITE, bytes.length
            - POS_DATA_WRITE);
    }

    public void setWriteResponse()
    {
        byte[] tmp = { 0x00, 0x01, (byte) 0xff };
        bytes = tmp;
        S7Bytes.setWriteId(bytes);
    }

    public int getDbNum()
    {
        return S7Bytes.getShort(bytes, POS_DB);
    }

    public int getCount()
    {
        return S7Bytes.getShort(bytes, POS_COUNT);
    }

    public int getOffset()
    {
        return S7Bytes.getShort(bytes, POS_OFFSET);
    }

    public int getSlot()
    {
        return bytes[POS_SLOT] & 0x1F;
    }
}
