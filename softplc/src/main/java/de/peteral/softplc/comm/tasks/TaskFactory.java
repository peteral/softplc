package de.peteral.softplc.comm.tasks;

import de.peteral.softplc.comm.common.ServerDataEvent;
import de.peteral.softplc.model.CommunicationTask;

/**
 * TaskFactory implementations create {@link CommunicationTask} from
 * {@link ServerDataEvent}.
 *
 * @author peteral
 *
 */
public interface TaskFactory {
	/**
	 * Signals whether this factory instance can handle the event.
	 *
	 * @param dataEvent
	 *            event that needs to be translated into
	 *            {@link CommunicationTask}
	 * @return true - this instance can handle the event
	 */
	boolean canHandle(ServerDataEvent dataEvent);

	/**
	 * Creates a concrete {@link CommunicationTask} instance from a
	 * {@link ServerDataEvent}.
	 *
	 * @param dataEvent
	 *            server data event
	 * @param factory
	 * @return concrete {@link CommunicationTask} instance
	 */
	CommunicationTask createTask(ServerDataEvent dataEvent,
			CommunicationTaskFactory factory);

}
