package de.peteral.softplc.comm.tasks;

import java.util.ArrayList;
import java.util.List;

import de.peteral.softplc.comm.common.ServerDataEvent;
import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.ResponseFactory;
import de.peteral.softplc.model.SoftplcResponseFactory;
import de.peteral.softplc.model.SoftplcTaskFactory;
import de.peteral.softplc.reflection.AnnotationProcessor;

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
	private static final List<TaskFactory> TASK_FACTORIES = new ArrayList<>();
	private static final List<ResponseFactory> RESPONSE_FACTORIES = new ArrayList<>();

	static {
		new AnnotationProcessor<TaskFactory>(SoftplcTaskFactory.class)
				.loadAnnotations(TASK_FACTORIES);

		new AnnotationProcessor<ResponseFactory>(SoftplcResponseFactory.class)
				.loadAnnotations(RESPONSE_FACTORIES);
	}

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
