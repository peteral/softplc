package eisenmann.connector.plc.ra.virtualplc.telegram.s7.header;

import eisenmann.connector.plc.ra.virtualplc.telegram.s7.S7Bytes;

// MPI 8 Byte Offset
// static MPIB1 S7RWB1 =
// {
// 0x32,
// 0x01, Request, 0x03 Response
// {0x00, 0x00, 0x02, 0x00},
// 0, Len Block 1 wird später ausgefllt
// 0 bleibt 0 !
// };

// typedef struct
// {
// BYTE Hex32;
// BYTE Code;
//
// BYTE Ub2[4];
// WORD LenBl1;
// WORD LenBl2;
// } __PACKED__ MPIB1, *PMPIB1, far *LPMPIB1;

public class HeaderS7Request
    extends HeaderS7
    implements TelegramHeader

{
    private final byte[] initValues = { 0x32, 0x01, 0x00, 0x00, 0x02, 0x00,
        0x00, 0x00, 0x00, 0x00 };

    private static int LEN = 10;

    public static final int POS_S7_CMD_LEN = 6;

    public HeaderS7Request()
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
        S7Bytes.setShort(bytes, len, POS_S7_CMD_LEN);
    }

    public int getCommandLen()
    {
        return S7Bytes.getShort(bytes, POS_S7_CMD_LEN);
    }

}
