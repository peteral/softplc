package de.peteral.softplc.comm.tasks;

import java.nio.channels.SocketChannel;

import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.PutGetServer;

/**
 * {@link CommunicationTask} which handles the read multiple bytes telegram.
 *
 * @author peteral
 *
 */
public class ReadBytesTask extends AbstractCommunicationTask {

	/**
	 * Initialize new instance.
	 *
	 * @param server
	 *            sever
	 * @param socket
	 *            client socket
	 * @param factory
	 */
	public ReadBytesTask(PutGetServer server, SocketChannel socket,
			CommunicationTaskFactory factory) {
		super(server, socket, factory);
	}

	@Override
	protected void doExecute(Cpu cpu) {
		// TODO Auto-generated method stub

	}

}
