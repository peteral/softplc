package de.peteral.softplc.comm.telegram;

/**
 * Response to Connect Command.
 * <p>
 * The only variable data is PDU size.
 * <p>
 * The max number of bytes transferred within one read / write command is
 * pduSize-20
 *
 * @author peteral
 *
 */
public class IsoConnectResult {
	private final int pduSize;

	/**
	 * Creates a new instance.
	 *
	 * @param maxDataSize
	 *            maximal supported data data block size per telegram
	 */
	public IsoConnectResult(int maxDataSize) {
		pduSize = maxDataSize + 20;
	}

	/**
	 *
	 * @return byte stream representation
	 */
	public byte[] getData() {
		return new byte[] { 0x32, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08,
				0x00, 0x00, 0x00, 0x00, (byte) 0xF0, 0x00, 0x00, 0x00, 0x00,
				0x00, (byte) (pduSize / 256), (byte) (pduSize % 256) };
	};
}
