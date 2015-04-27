package de.peteral.softplc.comm.tasks;

import de.peteral.softplc.comm.common.ServerDataEvent;
import de.peteral.softplc.model.CommunicationTask;
import eisenmann.connector.plc.ra.virtualplc.telegram.s7.S7TelegrammFactory;
import eisenmann.connector.plc.ra.virtualplc.telegram.s7.TelReadRequest;

/**
 * {@link TaskFactory} for read bytes telegram.
 *
 * @author peteral
 *
 */
public class ReadBytesTaskFactory implements TaskFactory {

	@Override
	public boolean canHandle(ServerDataEvent dataEvent) {
		return (S7TelegrammFactory.get().newTelegam(dataEvent.getData())  instanceof  TelReadRequest);
	}

	@Override
	public CommunicationTask createTask(ServerDataEvent dataEvent,
			CommunicationTaskFactory factory) {
		TelReadRequest request = (TelReadRequest) S7TelegrammFactory.get().newTelegam(dataEvent.getData()); 
		return new ReadBytesTask(dataEvent.getServer(), dataEvent.getSocket(), factory, "DB"+request.getDbNum(), request.getOffset(), request.getCountDef());
	}

}
