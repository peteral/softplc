package de.peteral.softplc.comm.telegram;

/**
 * Connect command sent by client.
 * <p>
 *
 * @author peteral
 *
 */
public class IsoConnectCommand {
	/**
	 * @return byte representation of the command.
	 */
	public byte[] getData() {
		return new byte[] { 0x32, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08,
				0x00, 0x00, (byte) 0xf0, 0x00, 0x00, 0x01, 0x00, 0x01, 0x01,
				(byte) 0xe0 };
	}
}
