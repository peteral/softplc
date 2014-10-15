package de.peteral.softplc.comm.tasks;

import de.peteral.softplc.comm.common.ServerDataEvent;
import de.peteral.softplc.model.CommunicationTask;

/**
 * {@link TaskFactory} for the write bytes telegram.
 *
 * @author peteral
 *
 */
public class WriteBytesTaskFactory implements TaskFactory {

	@Override
	public boolean canHandle(ServerDataEvent dataEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CommunicationTask createTask(ServerDataEvent dataEvent,
			CommunicationTaskFactory factory) {
		// TODO Auto-generated method stub
		return null;
	}

}
