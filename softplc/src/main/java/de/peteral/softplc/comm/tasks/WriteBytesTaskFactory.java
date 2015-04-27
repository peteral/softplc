package de.peteral.softplc.comm.tasks;

import de.peteral.softplc.comm.common.ServerDataEvent;
import de.peteral.softplc.model.CommunicationTask;
import eisenmann.connector.plc.ra.virtualplc.telegram.s7.S7TelegrammFactory;
import eisenmann.connector.plc.ra.virtualplc.telegram.s7.TelWriteRequest;

/**
 * {@link TaskFactory} for the write bytes telegram.
 *
 * @author peteral
 *
 */
public class WriteBytesTaskFactory implements TaskFactory {

	@Override
	public boolean canHandle(ServerDataEvent dataEvent) {
		return (S7TelegrammFactory.get().newTelegam(dataEvent.getData())  instanceof  TelWriteRequest);
	}

	@Override
	public CommunicationTask createTask(ServerDataEvent dataEvent,
			CommunicationTaskFactory factory) {
		TelWriteRequest request = (TelWriteRequest) S7TelegrammFactory.get().newTelegam(dataEvent.getData()); 
		return new WriteBytesTask(dataEvent.getServer(), dataEvent.getSocket(), factory, "DB"+request.getDbNum(), request.getOffset(), request.getWrtieData());
	}

}
