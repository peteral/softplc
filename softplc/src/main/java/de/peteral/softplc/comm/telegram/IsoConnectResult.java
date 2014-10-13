package de.peteral.softplc.comm.telegram;

import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.ResponseFactory;

/**
 * Connect confirmation message.
 * <p>
 * RFC header + 1 byte result
 * <p>
 * Server answers with this message in order to confirm ISO connection
 * established.
 *
 * @author peteral
 *
 */
// TODO handling for invalid CPU number
public class IsoConnectResult implements ResponseFactory {
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

	@Override
	public boolean canHandle(CommunicationTask task) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte[] createResponse(CommunicationTask task) {
		// TODO Auto-generated method stub
		return null;
	}
}
