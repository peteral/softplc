package de.peteral.softplc.comm.telegram;

import java.util.Arrays;

import de.peteral.softplc.comm.CommunicationTaskFactory;
import de.peteral.softplc.comm.ServerDataEvent;
import de.peteral.softplc.comm.tasks.PutGetConnectTask;
import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.TaskFactory;

/**
 * This is a connect command on PUT/GET protocol level.
 * <p>
 * It is sent by the client after the ISO connection has been established.
 *
 * @author peteral
 *
 */
public class PutGetConnectCommand implements TaskFactory {
	private static final byte[] COMMAND = { 0x32, 0x01, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x08, 0x00, 0x00, (byte) 0xf0, 0x00, 0x00, 0x01, 0x00, 0x01,
		0x01, (byte) 0xe0 };

	@Override
	public boolean canHandle(ServerDataEvent dataEvent) {
		return Arrays.equals(COMMAND, dataEvent.getData());
	}

	@Override
	public CommunicationTask createTask(ServerDataEvent dataEvent,
			CommunicationTaskFactory factory) {

		return new PutGetConnectTask(dataEvent.getServer(),
				dataEvent.getSocket(), ClientChannelCache.getInstance()
						.getSlot(dataEvent.getSocket()), factory);
	}
}
