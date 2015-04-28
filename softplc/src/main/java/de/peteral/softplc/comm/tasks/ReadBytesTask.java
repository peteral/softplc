package de.peteral.softplc.comm.tasks;

import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.MemoryAccessViolationException;
import de.peteral.softplc.model.PutGetServer;
import eisenmann.connector.plc.ra.virtualplc.telegram.s7.S7TelegrammFactory;
import eisenmann.connector.plc.ra.virtualplc.telegram.s7.TelReadResponse;

/**
 * {@link CommunicationTask} which handles the read multiple bytes telegram.
 *
 * @author peteral
 *
 */
public class ReadBytesTask extends AbstractCommunicationTask {
	private  final Logger LOGGER = Logger.getLogger("communication");

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
		//LOGGER.warning("Execute read: mem=" + memoryArea + ", off="+ offset + ", len=" +length + ", CPU-Slot="+ cpu.getSlot());
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
		TelReadResponse response = S7TelegrammFactory.get().newReadResponse(length, memoryArea, offset, data);
		byte[] bytes = response.getBytes();
		boolean doLog = false;
		for (byte b : data) {
			if(b != 0)
			{
				doLog=true;
			}
		}
		LOGGER.log((doLog) ? Level.FINE : Level.FINER, "Execute read getData: mem=" + memoryArea + ", off="+ offset + ", data=" + Arrays.toString(bytes));
		return bytes;
	}

//	public S7Telegram getTelegram() {
//		TelWriteResponse response = S7TelegrammFactory.get().newWriteResponse();
//		return response;
//	}
}
