package de.peteral.softplc.model;

import java.io.IOException;

/**
 * Provides PUT/GET protocol interface for accessing the memory of this
 * {@link Plc}.
 * <p>
 * Port - 102
 *
 * @author peteral
 *
 */
public interface PutGetServer {
	/**
	 * Starts listening and processing PUT/GET requests on defined port.
	 *
	 * @param plc
	 *            handles this plc during execution
	 * @throws IOException
	 */
	void start(Plc plc) throws IOException;

	/**
	 * Stops listening on configured port.
	 * 
	 * @throws IOException
	 */
	void stop() throws IOException;

	/**
	 * Registers a new observer.
	 *
	 * @param o
	 *            observer instance
	 */
	void addObserver(PutGetServerObserver o);

	/**
	 * Unregisters an observer.
	 *
	 * @param o
	 *            observer instance.
	 */
	void removeObserver(PutGetServerObserver o);
}
