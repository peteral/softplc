package de.peteral.softplc.comm.tasks;

import java.util.Arrays;
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
		byte[] data = ((WriteBytesTask)task).getData();
		LOGGER.fine("Write createResponse data=" + Arrays.toString(data));
		return data;
	}

}
