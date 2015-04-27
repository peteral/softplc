package eisenmann.connector.plc.ra.virtualplc.telegram.s7;

import java.util.Arrays;

import eisenmann.connector.plc.ra.virtualplc.telegram.s7.base.AbstractS7ResponseTelegramm;

public class TelReadResponse
    extends AbstractS7ResponseTelegramm
{
    private static final int POS_COUNT = 6;
    private static final int POS_DB = 8;
    private static final int POS_OFFSET = 12;//???
    private byte[] data;
    private final byte[] tmp = { 0x00, 0x01, 0x12, 0x0a, 0x10, 0x02,
        (byte) 0xcc, (byte) 0xcc, (byte) 0xdb, (byte) 0xdb, (byte) 0x84, 0x00,
        0x00, 0x00 };
    private final int dataOffset = tmp.length;

    public TelReadResponse(int count, String db, int offset, byte[] data)
    {
        db = db.replaceAll("DB", "");
        setReadResponse(count, Integer.parseInt(db), offset, data);
    }

    public TelReadResponse(int count, int db, int offset, byte[] data)
    {
        setReadResponse(count, db, offset, data);
    }

    public void setReadResponse(int count, int db, int offset, byte[] byteData)
    {
        data = new byte[tmp.length + byteData.length];
        System.arraycopy(tmp, 0, data, 0, tmp.length);
        S7Bytes.setShort(data, count, POS_COUNT);
        S7Bytes.setShort(data, db, POS_DB);
        S7Bytes.setShort(data, offset, POS_OFFSET);
        //TODO set data-len.which byte??
        System.arraycopy(byteData, 0, data, tmp.length, byteData.length);
        S7Bytes.setReadId(data);
    }

    public void setTelegramData(byte[] orgData)
    {
        rfcHeader.setValues(orgData, 0, rfcHeader.getTelegramLen());
        s7Header.setValues(orgData,
                           rfcHeader.getTelegramLen(),
                           s7Header.getTelegramLen());
        int offset = rfcHeader.getTelegramLen() + s7Header.getTelegramLen();
        data = new byte[orgData.length-offset];
        System.arraycopy(orgData, offset, data, 0, data.length);
    }

    //TODO aufteilen in basis und hier

    //TODO aufteilen in basis und hier
    @Override
    public int getTelegramLen()
    {
        return rfcHeader.getTelegramLen() + s7Header.getTelegramLen()
            + data.length;
    }


    //TODO aufteilen in basis und hier
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
        System.arraycopy(data,
                         0,
                         bytes,
                         rfcHeader.getTelegramLen() + s7Header.getTelegramLen(),
                         data.length);
        return bytes;
    }

    public TelReadResponse(byte[] data)
    {
        super(data);
    }

    public TelReadResponse(int readCount)
    {
        setReadResponse(0, 0, 0, new byte[readCount]);
    }

    @Override
    public int getError()
    {
        return s7Header.getResult();
    }

    public byte[] getData()
    {
        return Arrays.copyOfRange(data, dataOffset, data.length);
    }

    public int getDbNum()
    {
        return S7Bytes.getShort(data, POS_DB);
    }

    public int getCount()
    {
        return S7Bytes.getShort(data, POS_COUNT);
    }

    public int getOffset()
    {
        return S7Bytes.getShort(data, POS_OFFSET);
    }

    @Override
    public boolean isRead()
    {
        return true;
    }
}
