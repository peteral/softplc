package de.peteral.softplc.comm.tasks;

import java.nio.channels.SocketChannel;

import de.peteral.softplc.comm.RequestWorker;
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
	/**
	 * Initializes a new instance.
	 *
	 * @param server
	 * @param socket
	 * @param slot
	 * @param factory
	 */
	public IsoConnectTask(PutGetServer server, SocketChannel socket, int slot,
			CommunicationTaskFactory factory) {
		super(server, socket, slot, factory);
	}

	@Override
	protected void doExecute(Cpu cpu) {
		// do nothing
	}

	/**
	 * This task is always OK if it was scheduled to a CPU.
	 * <p>
	 * {@link RequestWorker} has special handling for the case the CPU does not
	 * exist.
	 *
	 * @return always true
	 */
	public boolean isOk() {
		return true;
	}

}
