package de.peteral.softplc.comm;

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
	private final int slot;

	/**
	 * Creates a new instance.
	 *
	 * @param server
	 *            server instance
	 * @param socket
	 *            socket to use for response
	 * @param slot
	 *            cpu slot number
	 */
	public AbstractCommunicationTask(PutGetServer server, SocketChannel socket,
			int slot) {
		this.server = server;
		this.socket = socket;
		this.slot = slot;
	}

	@Override
	public void execute(Cpu cpu) {
		doExecute(cpu);

		server.send(socket, getResponse());
	}

	protected abstract void doExecute(Cpu cpu);

	protected abstract byte[] getResponse();

	@Override
	public int getCpuSlot() {
		return slot;
	}

}
