package de.peteral.softplc.model;

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
	 */
	void start(Plc plc);

	/**
	 * Stops listening on configured port.
	 */
	void stop();

}
