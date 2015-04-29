package de.peteral.softplc.comm.tasks;

import java.util.Arrays;
import java.util.logging.Logger;

import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.ResponseFactory;

/**
 * {@link ResponseFactory} for set bit telegram.
 *
 * @author peteral
 *
 */
public class SetBitResponseFactory implements ResponseFactory {
	private  final Logger LOGGER = Logger.getLogger("communication");

	@Override
	public boolean canHandle(CommunicationTask task) {
		return task instanceof SetBitTask;
	}

	@Override
	public byte[] createResponse(CommunicationTask task) {
		byte[] data = ((SetBitTask)task).getData();
		LOGGER.fine("Write Bit createResponse data=" + Arrays.toString(data));
		return data;
	}

}
