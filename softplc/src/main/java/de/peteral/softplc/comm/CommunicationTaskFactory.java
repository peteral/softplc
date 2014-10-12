package de.peteral.softplc.comm;

import de.peteral.softplc.model.CommunicationTask;

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

	/**
	 * Creates a {@link CommunicationTask} from FRC1006 request.
	 *
	 * @param dataEvent
	 *            client request containing the data of the RFC1006 request.
	 * @return communication task instance
	 */
	public CommunicationTask createTask(ServerDataEvent dataEvent) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}
}
