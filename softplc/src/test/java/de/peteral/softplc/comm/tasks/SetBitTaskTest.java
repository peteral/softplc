package de.peteral.softplc.comm.tasks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
public class SetBitTaskTest {
	private static final int BIT_NUMBER = 4;
	private static final int OFFSET = 20;
	private static final String AREA = "DB100";
	@Mock
	private PutGetServer server;
	@Mock
	private SocketChannel socket;
	@Mock
	private CommunicationTaskFactory factory;
	private SetBitTask task;
	@Mock
	private Cpu cpu;
	@Mock
	private Memory memory;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		when(cpu.getMemory()).thenReturn(memory);

		task = new SetBitTask(server, socket, factory, AREA, OFFSET,
				BIT_NUMBER, true);
	}

	@Test
	public void doExecute_ValidMemoryAreaSetBit_IsOkReturnsTrueCorrectBitSet() {
		when(memory.readBytes(AREA, OFFSET, 1)).thenReturn(new byte[] { 1 });

		task.execute(cpu);

		verify(memory).writeBytes(AREA, OFFSET, new byte[] { 17 });

		assertTrue(task.isOk());
	}

	@Test
	public void doExecute_ValidMemoryAreaResetBit_IsOkReturnsTrueCorrectBitReset() {
		task = new SetBitTask(server, socket, factory, AREA, OFFSET,
				BIT_NUMBER, false);

		when(memory.readBytes(AREA, OFFSET, 1)).thenReturn(new byte[] { 17 });

		task.execute(cpu);

		verify(memory).writeBytes(AREA, OFFSET, new byte[] { 1 });

		assertTrue(task.isOk());
	}

	@Test
	public void doExecute_InvalidMemoryArea_IsOkReturnsFalse() {
		when(memory.readBytes(AREA, OFFSET, 1)).thenThrow(
				new MemoryAccessViolationException(""));

		task.execute(cpu);

		assertFalse(task.isOk());
	}
}
