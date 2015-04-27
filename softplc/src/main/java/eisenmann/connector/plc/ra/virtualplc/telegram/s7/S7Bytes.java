package eisenmann.connector.plc.ra.virtualplc.telegram.s7;

public class S7Bytes
{
    public static void setConnectId(byte[] dest)
    {
        dest[0] = (byte) 0xf0;
    }

    public static void setReadId(byte[] dest)
    {
        dest[0] = 0x04;
    }

    public static void setWriteId(byte[] dest)
    {
        dest[0] = 0x05;
    }

    public static void setShort(byte[] dest, int value, int pos)
    {
        dest[pos + 1] = (byte) ((value & 0xFF00) >> 8);
        dest[pos] = (byte) (value & 0xFF);
    }

    public static int getShort(byte[] src, int pos)
    {
        return ((src[pos + 1] & 0xFF) << 8) | (src[pos] & 0xFF);
    }

}
