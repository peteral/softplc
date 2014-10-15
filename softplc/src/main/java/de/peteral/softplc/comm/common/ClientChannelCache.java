package de.peteral.softplc.comm.common;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * Maps client channels to cpu numbers so incoming telegrams can be dispatched
 * to proper CPU based on the client channel.
 *
 * @author peteral
 *
 */
// TODO need thread-safety here
public class ClientChannelCache {
	private static ClientChannelCache instance = null;

	private final Map<SocketChannel, Integer> cache = new HashMap<>();

	private ClientChannelCache() {

	}

	/**
	 * @return provides access to the singleton instance
	 */
	public static ClientChannelCache getInstance() {
		if (instance == null) {
			instance = new ClientChannelCache();
		}

		return instance;
	}

	/**
	 * Looks up the CPU slot number this socket is assigned to.
	 *
	 * @param socket
	 * @return cpu slot number assigned to this socket
	 */
	public Integer getSlot(SocketChannel socket) {
		return cache.get(socket);
	}

	/**
	 * Registers a new client channel to a cpu slot number.
	 *
	 * @param socket
	 * @param slot
	 */
	public void addChannel(SocketChannel socket, int slot) {
		cache.put(socket, slot);
	}

	/**
	 * Unregisters a client channel after it has been closed.
	 *
	 * @param socket
	 */
	public void removeChannel(SocketChannel socket) {
		cache.remove(socket);
	}

	void clear() {
		cache.clear();
	}

	/**
	 * Installs a mocked instance as singleton.
	 * <p>
	 * For unit testing.
	 *
	 * @param mock
	 *            mock to be installed or null to uninstall the mock
	 */
	public static void installMock(ClientChannelCache mock) {
		instance = mock;
	}
}
