package de.peteral.softplc.comm.telegram;

import de.peteral.softplc.comm.ServerDataEvent;
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
	/**
	 * @return byte representation of the command.
	 */
	public byte[] getData() {
		return new byte[] { 0x32, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08,
				0x00, 0x00, (byte) 0xf0, 0x00, 0x00, 0x01, 0x00, 0x01, 0x01,
				(byte) 0xe0 };
	}

	@Override
	public boolean canHandle(ServerDataEvent dataEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CommunicationTask createTask(ServerDataEvent dataEvent) {
		// TODO Auto-generated method stub
		return null;
	}
}
