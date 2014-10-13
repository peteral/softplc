package de.peteral.softplc.comm.common;

import java.nio.channels.SocketChannel;

import de.peteral.softplc.model.PutGetServer;

/**
 * Encapsulates a request from a client.
 *
 * @author peteral
 *
 */
public class ServerDataEvent {
	private final PutGetServer server;
	private final SocketChannel socket;
	private final byte[] data;

	/**
	 * Creates a new instance.
	 *
	 * @param server
	 *            server instance
	 * @param socket
	 *            client socket to be used for sending a response
	 * @param data
	 *            request data
	 */
	public ServerDataEvent(PutGetServer server, SocketChannel socket,
			byte[] data) {
		this.server = server;
		this.socket = socket;
		this.data = data;
	}

	/**
	 * @return the server
	 */
	public PutGetServer getServer() {
		return server;
	}

	/**
	 * @return the socket
	 */
	public SocketChannel getSocket() {
		return socket;
	}

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}
}