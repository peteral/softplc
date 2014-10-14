package de.peteral.softplc.comm.tasks;

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
public class PutGetConnectResultFactory implements ResponseFactory {
	private byte[] getResult(int pduSize) {
		return new byte[] { 0x32, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08,
				0x00, 0x00, 0x00, 0x00, (byte) 0xF0, 0x00, 0x00, 0x00, 0x00,
				0x00, (byte) (pduSize / 256), (byte) (pduSize % 256) };
	}

	@Override
	public boolean canHandle(CommunicationTask task) {
		return task instanceof PutGetConnectTask;
	}

	@Override
	public byte[] createResponse(CommunicationTask task) {
		PutGetConnectTask connectTask = (PutGetConnectTask) task;
		return getResult(connectTask.getMaxDataSize() + 20);
	};
}
