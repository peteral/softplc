package de.peteral.softplc.comm.common;

import java.nio.channels.SocketChannel;

import de.peteral.softplc.model.NetworkInterface;

/**
 * Encapsulates a request from a client.
 *
 * @author peteral
 *
 */
public class ServerDataEvent {
	private final NetworkInterface server;
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
	public ServerDataEvent(NetworkInterface server, SocketChannel socket,
			byte[] data) {
		this.server = server;
		this.socket = socket;
		this.data = data;
	}

	/**
	 * @return the server
	 */
	public NetworkInterface getNetworkInterface() {
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