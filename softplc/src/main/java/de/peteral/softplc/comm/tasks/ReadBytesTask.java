package de.peteral.softplc.comm.tasks;

import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.logging.Logger;

import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.MemoryAccessViolationException;
import de.peteral.softplc.model.PutGetServer;
import eisenmann.connector.plc.ra.virtualplc.telegram.s7.S7TelegrammFactory;
import eisenmann.connector.plc.ra.virtualplc.telegram.s7.TelWriteResponse;

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
		LOGGER.info("Execute read: mem=" + memoryArea + ", off="+ offset + ", len=" +length);
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
		TelWriteResponse response = S7TelegrammFactory.get().newWriteResponse();
		LOGGER.warning("Execute read getData: mem=" + memoryArea + ", off="+ offset + ", data=" + Arrays.toString(response.getBytes()));
		return response.getBytes();
	}

//	public S7Telegram getTelegram() {
//		TelWriteResponse response = S7TelegrammFactory.get().newWriteResponse();
//		return response;
//	}
}
