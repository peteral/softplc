package de.peteral.softplc.comm.tasks;

import de.peteral.softplc.comm.common.ServerDataEvent;
import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.ResponseFactory;

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
			new IsoConnectTaskFactory(), new PutGetConnectTaskFactory(),
		new ReadBytesTaskFactory(), new WriteBytesTaskFactory(),
		new SetBitTaskFactory() };

	private static final ResponseFactory RESPONSE_FACTORIES[] = {
		new IsoConnectResponseFactory(),
			new PutGetConnectResponseFactory(), new ReadBytesResponseFactory(),
			new WriteBytesResponseFactory(), new SetBitResponseFactory() };

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
				return factory.createTask(dataEvent, this);
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
