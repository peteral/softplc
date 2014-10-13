package de.peteral.softplc.model;

/**
 * Creates a byte array to be sent back to a client from a
 * {@link CommunicationTask} instance.
 *
 * @author peteral
 *
 */
public interface ResponseFactory {
	/**
	 * Signals whether this factory is able to handle the concrete
	 * {@link CommunicationTask}.
	 *
	 * @param task
	 *            task to be checked
	 * @return true - this {@link ResponseFactory} instance can handle the task
	 */
	boolean canHandle(CommunicationTask task);

	/**
	 * Creates a byte array to be sent back to a client from a
	 * {@link CommunicationTask} instance.
	 *
	 * @param task
	 *            communication task
	 * @return byte array to be sent back to a client
	 */
	byte[] createResponse(CommunicationTask task);

}
