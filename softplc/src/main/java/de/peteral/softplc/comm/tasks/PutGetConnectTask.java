package de.peteral.softplc.comm.tasks;

import java.nio.channels.SocketChannel;

import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.PutGetServer;

/**
 * This task only gets the maximum data block size from the CPU.
 *
 * @author peteral
 */
public class PutGetConnectTask extends AbstractCommunicationTask {
	private int maxDataSize;

	/**
	 * Initialize a new instance.
	 *
	 * @param server
	 * @param socket
	 * @param factory
	 */
	public PutGetConnectTask(PutGetServer server, SocketChannel socket,
			CommunicationTaskFactory factory) {
		super(server, socket, factory);
	}

	@Override
	protected void doExecute(Cpu cpu) {
		maxDataSize = cpu.getMaxDataSize();
	}

	/**
	 *
	 * @return maximum block size supported by the CPU
	 */
	public int getMaxDataSize() {
		return maxDataSize;
	}

}
