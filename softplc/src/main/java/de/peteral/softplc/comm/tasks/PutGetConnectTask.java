package de.peteral.softplc.comm.tasks;

import java.nio.channels.SocketChannel;

import de.peteral.softplc.comm.AbstractCommunicationTask;
import de.peteral.softplc.comm.CommunicationTaskFactory;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.PutGetServer;

public class PutGetConnectTask extends AbstractCommunicationTask {

	public PutGetConnectTask(PutGetServer server, SocketChannel socket,
			int slot, CommunicationTaskFactory factory) {
		super(server, socket, slot, factory);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doExecute(Cpu cpu) {
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * @return maximum block size supported by the CPU
	 */
	public int getMaxDataSize() {
		// TODO Auto-generated method stub
		return 0;
	}

}
