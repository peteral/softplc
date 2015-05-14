package de.peteral.softplc.comm.tasks;

import java.nio.channels.SocketChannel;

import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.PutGetServer;

/**
 * Implements functionality common for all tasks.
 *
 * @author peteral
 *
 */
public abstract class AbstractCommunicationTask implements CommunicationTask {

	private final PutGetServer server;
	private final SocketChannel socket;
	private final CommunicationTaskFactory factory;

	/**
	 * Creates a new instance.
	 *
	 * @param server
	 *            server instance
	 * @param socket
	 *            socket to use for response
	 * @param factory
	 */
	public AbstractCommunicationTask(PutGetServer server, SocketChannel socket,
			CommunicationTaskFactory factory) {
		this.server = server;
		this.socket = socket;
		this.factory = factory;
	}

	@Override
	public void execute(Cpu cpu) {
		doExecute(cpu);

		sendResponse();
	}

	/**
	 * Sends response to the client.
	 */
	public void sendResponse() {
		byte[] response = factory.createResponse(this);
		server.send(socket, response);
	}

	protected abstract void doExecute(Cpu cpu);

	@Override
	public void onInvalidCpu(int slot) {
	}
}
