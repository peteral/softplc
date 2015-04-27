package eisenmann.connector.plc.ra.virtualplc.telegram.s7;

import eisenmann.connector.plc.ra.virtualplc.telegram.s7.base.AbstractS7RequestTelegramm;
import eisenmann.connector.plc.ra.virtualplc.telegram.s7.base.AbstractS7ResponseTelegramm;

public class TelConnectRequest
    extends AbstractS7RequestTelegramm//AbstractByteTelegram
{
    public TelConnectRequest(byte[] data)
    {
        super(data);
    }

    //private static final int OFFSET_RACK_AND_SLOT = 18;

    @Override
    public int getError()
    {
        if ( s7data.getSlot() > 0 ) //(bytes[OFFSET_RACK_AND_SLOT] & 0x1F) > 0 )
        {
            return 0;
        }
        return -1;
    }

    public TelConnectRequest(int slot)
    {
        setConnectRequest(slot);
    }

    @Override
    public boolean isConnect()
    {
        return true;
    }

    @Override
    public AbstractS7ResponseTelegramm getResponse()
    {
        TelConnectResponse response = new TelConnectResponse();
        return response;
    }

    public int getSlot()
    {
    	return s7data.getSlot();
    }
//    private static final int OFFSET_RACK_AND_SLOT = 18;
//
//    private static final byte WILDCARD = (byte) 0xFF;
//    // 1 = PG, 2 = visu, 3 = other; use 2
//    private static final byte TYPE = WILDCARD;
//    // rack << 5 | slot
//    private static final byte RACK_AND_SLOT = WILDCARD;
//
//    private static int LEN = 22;
//
//    //TODO here only slot is set, all other are default
//    public TelConnectRequest(int slot)
//    {
//        byte[] DATA = { 0x03, 0x00, 0x00, 0x1A, // +0 RFC header
//            0x15, (byte) 0xE0, 0x00, 0x00, 0x00, 0x01, 0x00, // +4 ISO header
//            (byte) 0xC1, 0x02, 0x01, 0x00, // +11 source
//            (byte) 0xC2, 0x02, TYPE, 0x00,//RACK_AND_SLOT, // +15 destination
//            (byte) 0xC0, 0x01, 0x09 // +19 TPUD
//            };
//        DATA[OFFSET_RACK_AND_SLOT] = (byte) slot;
//        super.setValues(DATA);
//    }
//
//    @Override
//    public int getTelegramLen()
//    {
//        return LEN;
//    }
//
//    @Override
//    public int getError()
//    {
//        if ( (bytes[OFFSET_RACK_AND_SLOT] & 0x1F) > 0 )
//        {
//            return 0;
//        }
//        return -1;
//    }
}
