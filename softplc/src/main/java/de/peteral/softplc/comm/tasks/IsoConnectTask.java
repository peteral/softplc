package de.peteral.softplc.comm.tasks;

import java.nio.channels.SocketChannel;

import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.PutGetServer;

public class IsoConnectTask extends AbstractCommunicationTask {

	private final int rack;

	public IsoConnectTask(PutGetServer server, SocketChannel socket, int slot,
			int rack, CommunicationTaskFactory factory) {
		super(server, socket, slot, factory);
		this.rack = rack;
	}

	@Override
	protected void doExecute(Cpu cpu) {
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * @return true - requested rack / slot is valid
	 */
	public boolean isOk() {
		// TODO Auto-generated method stub
		return false;
	}

}
