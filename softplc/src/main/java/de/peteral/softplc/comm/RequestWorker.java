package de.peteral.softplc.comm;

import java.util.LinkedList;
import java.util.List;

import de.peteral.softplc.comm.common.ServerDataEvent;
import de.peteral.softplc.comm.tasks.CommunicationTaskFactory;
import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.Plc;
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
	private final CommunicationTaskFactory communicationTaskFactory;
	private final Plc plc;

	/**
	 * Creates a new instance.
	 *
	 * @param plc
	 *            plc to work on
	 * @param communicationTaskFactory
	 *            communication task factory instance
	 */
	public RequestWorker(Plc plc,
			CommunicationTaskFactory communicationTaskFactory) {
		this.plc = plc;
		this.communicationTaskFactory = communicationTaskFactory;

	}

	/**
	 * Adds a new {@link ServerDataEvent} to the internal queue.
	 * <p>
	 * It will be processed asynchronously by the worker thread.
	 *
	 * @param event
	 */
	public void processData(ServerDataEvent event) {
		synchronized (queue) {
			queue.add(event);
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

			CommunicationTask task = communicationTaskFactory
					.createTask(dataEvent);

			if (task != null) {
				plc.getCpu(task.getCpuSlot()).addCommunicationTask(task);
			} else {
				// TODO warning - unknown incoming telegram
			}

			dataEvent.getServer().notifyObservers(new PutGetServerEvent());
		}
	}

	/**
	 * Stops the worker thread.
	 */
	public void cancel() {
		running = false;
	}
}
