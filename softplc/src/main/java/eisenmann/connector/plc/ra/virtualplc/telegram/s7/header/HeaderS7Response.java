package eisenmann.connector.plc.ra.virtualplc.telegram.s7.header;

import eisenmann.connector.plc.ra.virtualplc.telegram.s7.S7Bytes;

public class HeaderS7Response
    extends HeaderS7
    implements TelegramHeader

{
    private final byte[] initValues = { 0x32, 0x03, 0x00, 0x00, 0x02, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

    private static int LEN = 12;

    public static final int POS_S7_RESULT = 8;

    public HeaderS7Response()
    {
        setValues(initValues);
    }

    @Override
    public int getError()
    {
        return 0;
    }

    @Override
    public int getTelegramLen()
    {
        return LEN;
    }

    @Override
    public int getPosition()
    {
        return POS;
    }

    public void setCommandLen(int len)
    {
        S7Bytes.setShort(bytes, len, HeaderS7Request.POS_S7_CMD_LEN);
    }

    public int getCommandLen()
    {
        return S7Bytes.getShort(bytes, HeaderS7Request.POS_S7_CMD_LEN);
    }

    public void setResult(int res)
    {
        S7Bytes.setShort(bytes, res, POS_S7_RESULT);
    }

    @Override
    public int getResult()
    {
        return S7Bytes.getShort(bytes, POS_S7_RESULT);
    }

}
