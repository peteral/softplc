package de.peteral.softplc.comm.tasks;

import java.nio.channels.SocketChannel;

import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.PutGetServer;
import de.peteral.softplc.model.ResponseFactory;

/**
 * This task is created after a ISO connect. It does nothing and relies on the
 * behavior of the super class and the {@link ResponseFactory}.
 *
 * @author peteral
 *
 */
public class IsoConnectTask extends AbstractCommunicationTask {
	private boolean slotValid;

	/**
	 * Initializes a new instance.
	 *
	 * @param server
	 * @param socket
	 * @param factory
	 */
	public IsoConnectTask(PutGetServer server, SocketChannel socket,
			CommunicationTaskFactory factory) {
		super(server, socket, factory);
		this.slotValid = true;
	}

	/**
	 *
	 * invalidates this task when CPU is not found.
	 */
	public void invalidate() {
		slotValid = false;
	}

	@Override
	protected void doExecute(Cpu cpu) {
		// do nothing
	}

	/**
	 * @return always true - the CPU is valid
	 */
	public boolean isOk() {
		return slotValid;
	}

}
