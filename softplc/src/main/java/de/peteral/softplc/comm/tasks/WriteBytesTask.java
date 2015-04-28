package de.peteral.softplc.comm.tasks;

import java.nio.channels.SocketChannel;

import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.MemoryAccessViolationException;
import de.peteral.softplc.model.PutGetServer;
import eisenmann.connector.plc.ra.virtualplc.telegram.s7.S7TelegrammFactory;
import eisenmann.connector.plc.ra.virtualplc.telegram.s7.TelWriteResponse;

/**
 * {@link CommunicationTask} which handles the write bytes telegram.
 *
 * @author peteral
 *
 */
public class WriteBytesTask extends AbstractCommunicationTask {
	private final String memoryArea;
	private final int offset;
	private final byte[] data;
	private boolean ok;

	/**
	 * Initialize new instance.
	 *
	 * @param server
	 *            server
	 * @param socket
	 *            client socket
	 * @param factory
	 * @param data
	 * @param offset
	 * @param memoryArea
	 */
	public WriteBytesTask(PutGetServer server, SocketChannel socket,
			CommunicationTaskFactory factory, String memoryArea, int offset,
			byte[] data) {
		super(server, socket, factory);
		this.memoryArea = memoryArea;
		this.offset = offset;
		this.data = data;
	}

	@Override
	protected void doExecute(Cpu cpu) {
		try {
			cpu.getMemory().writeBytes(memoryArea, offset, data);
			ok = true;
		} catch (MemoryAccessViolationException e) {
			ok = false;
		}

	}

	/**
	 * @return true - writing memory was successful
	 */
	public boolean isOk() {
		return ok;
	}
	
	public byte[] getData() {
		TelWriteResponse response = S7TelegrammFactory.get().newWriteResponse();
		return response.getBytes();
	}

}
