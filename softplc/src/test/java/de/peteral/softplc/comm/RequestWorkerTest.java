package de.peteral.softplc.comm;

import static org.mockito.Mockito.*;

import java.nio.channels.SocketChannel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.comm.common.ClientChannelCache;
import de.peteral.softplc.comm.common.ServerDataEvent;
import de.peteral.softplc.comm.tasks.CommunicationTaskFactory;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.NetworkInterface;
import de.peteral.softplc.model.Plc;
import de.peteral.softplc.protocol.CommunicationTask;

@SuppressWarnings("javadoc")
public class RequestWorkerTest {

	private static final int CPU_SLOT = 10;
	@Mock
	private Plc plc;
	@Mock
	private CommunicationTaskFactory communicationTaskFactory;
	private RequestWorker worker;
	@Mock
	private NetworkInterface server;
	@Mock
	private SocketChannel socket;
	@Mock
	private ServerDataEvent event;
	@Mock
	private CommunicationTask task;
	@Mock
	private Cpu cpu;
	@Mock
	private ClientChannelCache cache;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		ClientChannelCache.installMock(cache);
		when(cache.getSlot(socket)).thenReturn(CPU_SLOT);

		when(event.getSocket()).thenReturn(socket);
		when(plc.getCpu(CPU_SLOT)).thenReturn(cpu);
		when(communicationTaskFactory.createTask(event)).thenReturn(task);
		when(event.getNetworkInterface()).thenReturn(server);
		when(plc.hasCpu(CPU_SLOT)).thenReturn(true);

		worker = new RequestWorker(plc, communicationTaskFactory);
	}

	@After
	public void teardown() {
		ClientChannelCache.installMock(null);
	}

	@Test(timeout = 2000)
	public void processData_WorkerRunning_CommunicationTaskCreatedAndDelegatedToCorrectCpu()
			throws InterruptedException {
		new Thread(worker).start();

		worker.processData(event);

		Thread.sleep(250);

		worker.cancel();

		verify(cpu).addCommunicationTask(task);
	}
}
