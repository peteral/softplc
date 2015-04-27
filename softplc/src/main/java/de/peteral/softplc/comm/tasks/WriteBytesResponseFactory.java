package de.peteral.softplc.comm.tasks;

import java.util.logging.Logger;

import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.ResponseFactory;

/**
 * {@link ResponseFactory} for the write bytes telegram.
 * 
 * @author peteral
 *
 */
public class WriteBytesResponseFactory implements ResponseFactory {
	private  final Logger LOGGER = Logger.getLogger("communication");

	@Override
	public boolean canHandle(CommunicationTask task) {
		return task instanceof WriteBytesTask;
	}

	@Override
	public byte[] createResponse(CommunicationTask task) {
		LOGGER.warning("Write createResponse data=" + ((WriteBytesTask)task).getData());
		return ((WriteBytesTask)task).getData();
	}

}
