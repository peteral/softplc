package de.peteral.softplc.comm;

import de.peteral.softplc.comm.telegram.IsoConnectCommand;
import de.peteral.softplc.comm.telegram.IsoConnectResult;
import de.peteral.softplc.comm.telegram.PutGetConnectCommand;
import de.peteral.softplc.comm.telegram.PutGetConnectResult;
import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.ResponseFactory;
import de.peteral.softplc.model.TaskFactory;

/**
 * Translates RF1006 byte arrays from / to communication tasks.
 * <p>
 * Protocol structure (<a href=
 * 'http://tools.ietf.org/html/rfc1006'>http://tools.ietf.org/html/rfc1006</a>):
 * <table>
 * </table>
 *
 * @author peteral
 *
 */
public class CommunicationTaskFactory {
	private static final TaskFactory TASK_FACTORIES[] = {
		new IsoConnectCommand(), new PutGetConnectCommand() };

	private static final ResponseFactory RESPONSE_FACTORIES[] = {
			new IsoConnectResult(), new PutGetConnectResult() };

	/**
	 * Creates a {@link CommunicationTask} from FRC1006 request.
	 *
	 * @param dataEvent
	 *            client request containing the data of the RFC1006 request.
	 * @return communication task instance
	 */
	public CommunicationTask createTask(ServerDataEvent dataEvent) {
		for (TaskFactory factory : TASK_FACTORIES) {
			if (factory.canHandle(dataEvent)) {
				return factory.createTask(dataEvent);
			}
		}
		return null;
	}

	/**
	 * Creates a full RFC1006 response from Put/Get byte array returned by a
	 * CommunicationTask
	 *
	 * @param task
	 * @return response as byte array.
	 */
	public byte[] createResponse(CommunicationTask task) {
		for (ResponseFactory factory : RESPONSE_FACTORIES) {
			if (factory.canHandle(task)) {
				return factory.createResponse(task);
			}
		}
		return null;
	}
}
