package de.peteral.softplc.comm.tasks;

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
public class IsoConnectResult implements ResponseFactory {
	private static final byte[] GOOD_RESPONSE = { 0x03, 0x00, 0x00, 0x01,
			(byte) 0xD0 };
	// TODO check bad response byte stream
	private static final byte[] BAD_RESPONSE = { 0x03, 0x00, 0x00, 0x01,
			(byte) 0x00 };

	@Override
	public boolean canHandle(CommunicationTask task) {
		return task instanceof IsoConnectTask;
	}

	@Override
	public byte[] createResponse(CommunicationTask task) {
		IsoConnectTask connectTask = (IsoConnectTask) task;

		return (connectTask.isOk()) ? GOOD_RESPONSE : BAD_RESPONSE;
	}
}
