package de.peteral.softplc.comm;

import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

import de.peteral.softplc.model.PutGetServerEvent;

/**
 * Asynchronously processes data incoming on the server socket.
 *
 * @author peteral
 *
 */
public class RequestWorker implements Runnable {
	private final List<ServerDataEvent> queue = new LinkedList<>();
	private boolean running;

	/**
	 * Adds a new {@link ServerDataEvent} to the internal queue.
	 * <p>
	 * It will be processed asynchronously by the worker thread.
	 *
	 * @param server
	 * @param socket
	 * @param data
	 * @param count
	 */
	public void processData(PutGetServerImpl server, SocketChannel socket,
			byte[] data, int count) {
		byte[] dataCopy = new byte[count];
		System.arraycopy(data, 0, dataCopy, 0, count);
		synchronized (queue) {
			queue.add(new ServerDataEvent(server, socket, dataCopy));
			queue.notify();
		}
	}

	@Override
	public void run() {
		ServerDataEvent dataEvent;
		running = true;

		while (running) {
			// Wait for data to become available
			synchronized (queue) {
				while (queue.isEmpty()) {
					try {
						queue.wait();
					} catch (InterruptedException e) {
					}
				}
				dataEvent = queue.remove(0);
			}

			// TODO implementation goes here
			// create communication task and send it to proper CPU instance
			// Return to sender

			// this should be invoked within the communication task
			dataEvent.server.send(dataEvent.socket, dataEvent.data);

			// TODO provide data to PGSE
			dataEvent.server.notifyObservers(new PutGetServerEvent());
		}
	}

	/**
	 * Stops the worker thread.
	 */
	public void cancel() {
		running = false;
	}
}
