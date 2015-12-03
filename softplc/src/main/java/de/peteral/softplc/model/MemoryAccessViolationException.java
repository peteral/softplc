package de.peteral.softplc.model;

import de.peteral.softplc.protocol.CommunicationTask;

/**
 * This exception is thrown when the {@link Program} or a
 * {@link CommunicationTask} attempts to access invalid memory area.
 *
 * @author peteral
 *
 */
public class MemoryAccessViolationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates new instance.
	 *
	 * @param message
	 *            message describing the origin of this exception.
	 */
	public MemoryAccessViolationException(String message) {
		super(message);
	}

}
