package de.peteral.softplc.comm.tasks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
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
public class WriteBytesTaskTest {
	private static final String MEMORY_AREA = "DB100";
	private static final int OFFSET = 20;
	private static final byte[] DATA = { 0x01, 0x02, 0x03, 0x04 };
	@Mock
	private PutGetServer server;
	@Mock
	private SocketChannel socket;
	@Mock
	private CommunicationTaskFactory factory;
	private WriteBytesTask task;
	@Mock
	private Cpu cpu;
	@Mock
	private Memory memory;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		when(cpu.getMemory()).thenReturn(memory);

		task = new WriteBytesTask(server, socket, factory, MEMORY_AREA, OFFSET,
				DATA);
	}

	@Test
	public void doExecute_WriteOk_IsOkReturnsTrue() {
		task.doExecute(cpu);

		verify(memory).writeBytes(MEMORY_AREA, OFFSET, DATA);

		assertTrue(task.isOk());
	}

	@Test
	public void doExecute_WriteNotOk_IsOkReturnsFalse() {
		doThrow(new MemoryAccessViolationException("")).when(memory)
				.writeBytes(MEMORY_AREA, OFFSET, DATA);

		task.doExecute(cpu);

		assertFalse(task.isOk());
	}
}
