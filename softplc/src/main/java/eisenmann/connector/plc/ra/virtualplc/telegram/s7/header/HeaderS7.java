package eisenmann.connector.plc.ra.virtualplc.telegram.s7.header;

import eisenmann.connector.plc.ra.virtualplc.telegram.s7.S7;
import eisenmann.connector.plc.ra.virtualplc.telegram.s7.base.AbstractByteTelegram;

public abstract class HeaderS7
    extends AbstractByteTelegram
{
    private static int POS_RR = 1;
    protected static int POS = 8;

    public boolean isRequest()
    {
        if ( getBytes().length < getTelegramLen() )
        {
            return false;
        }
        return getBytes()[POS_RR] == S7.TEL_REQUEST;
    }

    public boolean isResponse()
    {
        if ( getBytes().length < getTelegramLen() )
        {
            return false;
        }
        return getBytes()[POS_RR] == S7.TEL_RESPONSE;
    }

	public int getResult() {
		// TODO Auto-generated method stub
		return -1;
	}

}
