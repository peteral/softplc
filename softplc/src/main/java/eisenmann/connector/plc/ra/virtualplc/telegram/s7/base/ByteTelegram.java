package eisenmann.connector.plc.ra.virtualplc.telegram.s7.base;

public interface ByteTelegram
{
    byte[] getBytes();

    //byte[] getRawBytes();

    boolean setValues(byte[] values);

    String getInfo();

    int getError();

    boolean isOK();

    int getTelegramLen();
}
