package de.peteral.softplc.comm.tasks;

import java.nio.channels.SocketChannel;

import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.MemoryAccessViolationException;
import de.peteral.softplc.model.PutGetServer;

/**
 * {@link CommunicationTask} which handles the read multiple bytes telegram.
 *
 * @author peteral
 *
 */
public class ReadBytesTask extends AbstractCommunicationTask {

	private final String memoryArea;
	private final int offset;
	private byte[] data;
	private final int length;

	/**
	 * Initialize new instance.
	 *
	 * @param server
	 *            sever
	 * @param socket
	 *            client socket
	 * @param factory
	 * @param offset
	 * @param memoryArea
	 * @param length
	 */
	public ReadBytesTask(PutGetServer server, SocketChannel socket,
			CommunicationTaskFactory factory, String memoryArea, int offset,
			int length) {
		super(server, socket, factory);
		this.memoryArea = memoryArea;
		this.offset = offset;
		this.length = length;
	}

	@Override
	protected void doExecute(Cpu cpu) {
		try {
			data = cpu.getMemory().readBytes(memoryArea, offset, length);
		} catch (MemoryAccessViolationException e) {
			data = null;
		}
	}

	/**
	 * @return data read from CPU or null when invalid
	 */
	public byte[] getData() {
		return data;
	}
}
