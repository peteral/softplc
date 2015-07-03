package de.peteral.softplc.comm.common;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Maps client channels to cpu numbers so incoming telegrams can be dispatched
 * to proper CPU based on the client channel.
 *
 * @author peteral
 *
 */
public class ClientChannelCache {
	private static ClientChannelCache instance = null;

	private final Map<String, Integer> cache = new HashMap<>();

	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

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
		lock.readLock().lock();
		try {
			return cache.get(getSocketAddress(socket));
		} finally {
			lock.readLock().unlock();
		}
	}

	private String getSocketAddress(SocketChannel socket) {
		try {
			return socket.getRemoteAddress().toString();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Registers a new client channel to a cpu slot number.
	 *
	 * @param socket
	 * @param slot
	 */
	public void addChannel(SocketChannel socket, int slot) {
		lock.writeLock().lock();
		try {
			cache.put(getSocketAddress(socket), slot);
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * Unregisters a client channel after it has been closed.
	 *
	 * @param socket
	 */
	public void removeChannel(SocketChannel socket) {
		lock.writeLock().lock();
		try {
			cache.remove(getSocketAddress(socket));
		} finally {
			lock.writeLock().unlock();
		}
	}

	void clear() {
		lock.writeLock().lock();
		try {
			cache.clear();
		} finally {
			lock.writeLock().unlock();
		}
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

	/**
	 * Returns current client count.
	 *
	 * @param slot
	 * @return number of currently connected clients
	 */
	public int getConnectionCount(int slot) {
		int result = 0;
		lock.readLock().lock();
		try {
			for (Integer s : cache.values()) {
				if (s.intValue() == slot) {
					result++;
				}
			}
		} finally {
			lock.readLock().unlock();
		}

		return result;
	}
}
