package de.peteral.softplc.model;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * Provides PUT/GET protocol interface for accessing the memory of this
 * {@link Plc}.
 * <p>
 * Port - 102
 *
 * @author peteral
 *
 */
public interface NetworkInterface {
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

	/**
	 * Notifies all registered observers about the corresponding event.
	 *
	 * @param event
	 */
	void notifyObservers(PutGetServerEvent event);

	/**
	 * Sends data to a connected client socket.
	 * 
	 * @param socket
	 *            client socket
	 * @param data
	 *            data to be sent.
	 */
	void send(SocketChannel socket, byte[] data);
}
