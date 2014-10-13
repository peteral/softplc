package de.peteral.softplc.comm.telegram;

import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.ResponseFactory;

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
public class PutGetConnectResult implements ResponseFactory {
	private int pduSize;

	/**
	 * Creates a new instance.
	 *
	 * @param maxDataSize
	 *            maximal supported data data block size per telegram
	 */
	public PutGetConnectResult() {
		// pduSize = maxDataSize + 20;
	}

	/**
	 *
	 * @return byte stream representation
	 */
	public byte[] getData() {
		return new byte[] { 0x32, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08,
				0x00, 0x00, 0x00, 0x00, (byte) 0xF0, 0x00, 0x00, 0x00, 0x00,
				0x00, (byte) (pduSize / 256), (byte) (pduSize % 256) };
	}

	@Override
	public boolean canHandle(CommunicationTask task) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte[] createResponse(CommunicationTask task) {
		// TODO Auto-generated method stub
		return null;
	};
}
