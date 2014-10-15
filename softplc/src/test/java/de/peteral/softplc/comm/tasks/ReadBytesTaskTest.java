package de.peteral.softplc.comm.tasks;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.nio.channels.SocketChannel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.Memory;
import de.peteral.softplc.model.MemoryAccessViolationException;
import de.peteral.softplc.model.PutGetServer;

@SuppressWarnings("javadoc")
public class ReadBytesTaskTest {
	private static final int OFFSET = 20;
	private static final String MEMORY_AREA = "DB100";
	private static final byte[] DATA = { 0x01, 0x02, 0x03, 0x04 };
	@Mock
	private PutGetServer server;
	@Mock
	private SocketChannel socket;
	@Mock
	private CommunicationTaskFactory factory;
	private ReadBytesTask task;
	@Mock
	private Cpu cpu;
	@Mock
	private Memory memory;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		when(cpu.getMemory()).thenReturn(memory);

		task = new ReadBytesTask(server, socket, factory, MEMORY_AREA, OFFSET,
				DATA.length);
	}

	@Test
	public void doExecute_ValidMemory_GetDataReturnsCorrectData() {
		when(memory.readBytes(MEMORY_AREA, OFFSET, DATA.length)).thenReturn(
				DATA);

		task.doExecute(cpu);

		assertArrayEquals(DATA, task.getData());
	}

	@Test
	public void doExecute_InvalidMemory_GetDataReturnsNull() {
		when(memory.readBytes(MEMORY_AREA, OFFSET, DATA.length)).thenThrow(
				new MemoryAccessViolationException(""));

		task.doExecute(cpu);

		assertNull(task.getData());
	}
}
