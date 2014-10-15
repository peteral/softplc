package de.peteral.softplc.comm.tasks;

import java.nio.channels.SocketChannel;

import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.MemoryAccessViolationException;
import de.peteral.softplc.model.PutGetServer;

/**
 * This {@link CommunicationTask} implements the set bit S7 telegram.
 *
 * @author peteral
 *
 */
public class SetBitTask extends AbstractCommunicationTask {
	private boolean ok;
	private final String area;
	private final int offset;
	private final int bitNumber;
	private final boolean value;

	/**
	 * Initialize new instance.
	 *
	 * @param server
	 *            server instance
	 * @param socket
	 *            client socket
	 * @param factory
	 * @param value
	 * @param bitNumber
	 * @param offset
	 * @param area
	 */
	public SetBitTask(PutGetServer server, SocketChannel socket,
			CommunicationTaskFactory factory, String area, int offset,
			int bitNumber, boolean value) {
		super(server, socket, factory);
		this.area = area;
		this.offset = offset;
		this.bitNumber = bitNumber;
		this.value = value;
	}

	@Override
	protected void doExecute(Cpu cpu) {
		try {
			byte[] data = cpu.getMemory().readBytes(area, offset, 1);

			if (value) {
				data[0] = (byte) (data[0] | (1 << bitNumber));
			} else {
				data[0] = (byte) (data[0] & (~(1 << bitNumber)));
			}

			cpu.getMemory().writeBytes(area, offset, data);

			ok = true;
		} catch (MemoryAccessViolationException e) {
			ok = false;
		}
	}

	/**
	 * @return true - task was executed successfully, false - memory access
	 *         violation
	 */
	public boolean isOk() {
		return ok;
	}
}
