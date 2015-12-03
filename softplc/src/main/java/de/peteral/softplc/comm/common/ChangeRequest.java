package de.peteral.softplc.comm.common;

import java.nio.channels.SocketChannel;

import de.peteral.softplc.comm.NetworkInterfaceImpl;

/**
 * Change request for the {@link SocketChannel} of {@link NetworkInterfaceImpl}.
 *
 * @author peteral
 *
 */
public class ChangeRequest {
	@SuppressWarnings("javadoc")
	public static final int REGISTER = 1;
	@SuppressWarnings("javadoc")
	public static final int CHANGEOPS = 2;

	private final SocketChannel socket;
	private final int type;
	private final int ops;

	/**
	 * Creates a new instance.
	 *
	 * @param socket
	 *            socket channel targeted by this change request.
	 * @param type
	 *            change request type
	 * @param ops
	 *            change request parameters
	 */
	public ChangeRequest(SocketChannel socket, int type, int ops) {
		this.socket = socket;
		this.type = type;
		this.ops = ops;
	}

	/**
	 * @return the socket
	 */
	public SocketChannel getSocket() {
		return socket;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the ops
	 */
	public int getOps() {
		return ops;
	}
}
