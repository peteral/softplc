package eisenmann.connector.plc.ra.virtualplc.telegram.s7;

public interface S7
{
    byte CMD_READ = 0x04;
    byte CMD_WRITE = 0x05;
    byte CMD_CON = (byte) 0xf0;
    byte CMD_CON2 = (byte) 0x02;

    byte TEL_REQUEST = 0x01;
    byte TEL_RESPONSE = 0x03;

    int POS_TYPE_RR = 8;
    int POS_TYPE_REQEST_RW = 17;
    int POS_TYPE_RESPONSE_RW = 19;
    
    String TYPE_DB = "DB";
    
    int OK = 0;
    int ERR_BASE = -1000;
    int ERR_TOO_SMALL = ERR_BASE - 1;
    int ERR_TOO_LASRGE = ERR_BASE - 2;
}
