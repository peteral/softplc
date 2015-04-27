package de.peteral.softplc.comm.tasks;

import java.util.logging.Logger;

import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.ResponseFactory;

/**
 * {@link ResponseFactory} for read bytes telegram.
 *
 * @author peteral
 *
 */
public class ReadBytesResponseFactory implements ResponseFactory {
	private final Logger LOGGER = Logger.getLogger("communication");

	@Override
	public boolean canHandle(CommunicationTask task) {
		return task instanceof ReadBytesTask;
	}

	@Override
	public byte[] createResponse(CommunicationTask task) {
		byte[] data = ((ReadBytesTask)task).getData();
		LOGGER.info("Read createResponse data=" + data);
		return data;
	}
}
