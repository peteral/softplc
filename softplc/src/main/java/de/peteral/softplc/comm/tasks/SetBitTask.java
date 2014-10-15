package de.peteral.softplc.comm.tasks;

import java.nio.channels.SocketChannel;

import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.PutGetServer;

/**
 * This {@link CommunicationTask} implements the set bit S7 telegram.
 *
 * @author peteral
 *
 */
public class SetBitTask extends AbstractCommunicationTask {
	/**
	 * Initialize new instance.
	 *
	 * @param server
	 *            server instance
	 * @param socket
	 *            client socket
	 * @param factory
	 */
	public SetBitTask(PutGetServer server, SocketChannel socket,
			CommunicationTaskFactory factory) {
		super(server, socket, factory);
	}

	@Override
	protected void doExecute(Cpu cpu) {
		// TODO Auto-generated method stub

	}

}
