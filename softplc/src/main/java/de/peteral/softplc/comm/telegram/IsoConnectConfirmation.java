package de.peteral.softplc.comm.telegram;

/**
 * Connect confirmation message.
 * <p>
 * RFC header + 1 byte result
 *
 * @author peteral
 *
 */
public class IsoConnectConfirmation {
	//
	/**
	 * byte array
	 * <p>
	 * client checks for size > 5 and data[5] & 0xF0 == 0xD00
	 *
	 * @return byte representation of this message.
	 */
	public byte[] getData() {
		return new byte[] { 0x03, 0x00, 0x00, 0x01, (byte) 0xD0 };
	}
}
