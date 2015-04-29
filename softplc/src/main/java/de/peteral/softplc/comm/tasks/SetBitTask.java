package de.peteral.softplc.comm.tasks;

import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.MemoryAccessViolationException;
import de.peteral.softplc.model.PutGetServer;
import eisenmann.connector.plc.ra.virtualplc.telegram.s7.S7TelegrammFactory;
import eisenmann.connector.plc.ra.virtualplc.telegram.s7.TelWriteResponse;

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
	private final  Logger logger = Logger.getLogger("communication");

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
			String address = area + ",X" + offset + "." + bitNumber;
			cpu.getMemory().setBit(address, value);
			logger.info("SetBit: " + address + " = " + value);
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
	
	public byte[] getData() {
		TelWriteResponse response = S7TelegrammFactory.get().newWriteResponse();
		return response.getBytes();
	}

}
