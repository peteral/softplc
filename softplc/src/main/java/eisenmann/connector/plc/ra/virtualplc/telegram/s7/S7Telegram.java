package eisenmann.connector.plc.ra.virtualplc.telegram.s7;

import eisenmann.connector.plc.ra.virtualplc.telegram.s7.base.ByteTelegram;


public interface S7Telegram
    extends ByteTelegram
{

    boolean isRequest();

    boolean isResponse();

    boolean isConnect();

    boolean isRead();

    boolean isWrite();

} 
