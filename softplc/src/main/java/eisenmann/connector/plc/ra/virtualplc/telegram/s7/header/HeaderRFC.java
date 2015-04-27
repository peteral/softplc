package eisenmann.connector.plc.ra.virtualplc.telegram.s7.header;

import eisenmann.connector.plc.ra.virtualplc.telegram.s7.S7Bytes;
import eisenmann.connector.plc.ra.virtualplc.telegram.s7.base.AbstractByteTelegram;

public class HeaderRFC
    extends AbstractByteTelegram
    implements TelegramHeader

{
    private static int POS = 0;
    private static int LEN = 7;
    //Positions in byte-array
    private static int POS_LENALL = 2;
    //ver, res, len,len, LI, code, EOT
    private final byte[] initValues = { 0x03, 0x00, 0x00, 0x04, 0x02,
        (byte) 0xf0, (byte) 0x80 };

    public HeaderRFC()
    {
        setValues(initValues);
    }

    @Override
    public int getError()
    {
        // TODO Auto-generated method stub
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

    public void setLenOfS7Telegramm(int len)
    {
        S7Bytes.setShort(bytes, len, POS_LENALL);
    }
}

//static MPIB1 S7RWB1 =
//{
//  0x32, 
//  0x01, 
//  {0x00, 0x00, 0x02, 0x00}, 
//  0, Len Block 1 wird spter ausgefllt */
//  0  /* bleibt 0 ! */
//};
///* ------------------------------------------------------------------ */
//
//static 
//BYTE _CmdWr []=
//{
//  0x05, 
//  0x01, 
//  0x12, 
//  0x0A,
//  0x10, /* S7Type */
//  0x02, /* 01 Bit= BOOL, 02 = BYTE */
// 
//  
//  0x00, /* Anzahl der Einheiten [6] */
//  0x00,
//   
//  0x00, /* [8] */
//  0x00, /* DB-Nummer */
//  
//  0x00, /* Speicherart [10] */
//  
//  0x00, /* Byteadresse [11] */
//  0x00, /* Byteadresse */
//  0x00, /* Byteadresse / BitAdresse */
//
//};
///* ------------------------------------------------------------------ */
// 
//static 
//BYTE _CmdRd []=
//{
//  0x04, 
//  0x01, 
//  0x12, 
//  0x0A,
//  0x10, /* S7Type */
//  0x02, /* 01 Bit= BOOL, 02 = BYTE */
//
//  
//  0x00, /* Anzahl der Einheiten [6] */
//  0x00,
//   
//  0x00, /* [8] */
//  0x00, /* DB-Nummer */
//  
//  0x00, /* Speicherart [10] */
//    
//  0x00, /* Byteadresse [11] */
//  0x00, /* Byteadresse */
//
//  0x00  /* Byteadresse / BitAdresse */
//};

//typedef struct
//{               
//  RCF_ISODATA RCFHead;// Länge 2, Achtung bei RS232 Lange 6
//  MPIB1   B1;
//  BYTE    Buf[1024];
//} __PACKED__ S7BLOCK, *PS7BLOCK, far *LPS7BLOCK;                

//RCF
//m_Tx.RCFHead.VersNr    = 0x03;
//m_Tx.RCFHead.Reserved  = 0x00;
//m_Tx.RCFHead.LI        = 0x02;
//m_Tx.RCFHead.Code      = 0xF0;
//m_Tx.RCFHead.PacketLen = 0;
//m_Tx.RCFHead.Eot       = 0x80;
//
//typedef struct
//{
//  BYTE VersNr;
//  BYTE Reserved;
//  WORD PacketLen;
//  BYTE LI;
//  BYTE Code;
//  BYTE Eot;
//} __PACKED__ RCF_ISODATA, RFC_HEADER; // 1.33

//MPI 8 Byte Offset
//static MPIB1 S7RWB1 =
//{
//  0x32, 
//  0x01, Request, 0x03 Response
//  {0x00, 0x00, 0x02, 0x00}, 
//  0, /*Len Block 1 wird später ausgefllt */
//  0  /* bleibt 0 ! */
//};

//typedef struct
//{  
//  BYTE Hex32;
//  BYTE Code;
//
//  BYTE Ub2[4];
//  WORD LenBl1;
//  WORD LenBl2;
//} __PACKED__ MPIB1, *PMPIB1, far *LPMPIB1;
