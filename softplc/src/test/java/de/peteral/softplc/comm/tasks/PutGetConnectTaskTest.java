package de.peteral.softplc.comm.tasks;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.nio.channels.SocketChannel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.PutGetServer;

@SuppressWarnings("javadoc")
public class PutGetConnectTaskTest {
	private static final int MAX_BLOCK_SIZE = 200;
	@Mock
	private PutGetServer server;
	@Mock
	private SocketChannel socket;
	@Mock
	private CommunicationTaskFactory factory;
	private PutGetConnectTask task;
	@Mock
	private Cpu cpu;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		task = new PutGetConnectTask(server, socket, 12, factory);
	}

	@Test
	public void exectue_Cpu_RetrievesMaxBlockSizeFromCpu() {
		when(cpu.getMaxDataSize()).thenReturn(MAX_BLOCK_SIZE);

		task.doExecute(cpu);

		assertEquals(MAX_BLOCK_SIZE, task.getMaxDataSize());
	}
}
