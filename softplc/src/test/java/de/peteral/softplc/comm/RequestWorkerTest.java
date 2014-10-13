package de.peteral.softplc.comm;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.channels.SocketChannel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.comm.common.ServerDataEvent;
import de.peteral.softplc.comm.tasks.CommunicationTaskFactory;
import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.Plc;
import de.peteral.softplc.model.PutGetServer;

@SuppressWarnings("javadoc")
public class RequestWorkerTest {

	private static final int CPU_SLOT = 10;
	@Mock
	private Plc plc;
	@Mock
	private CommunicationTaskFactory communicationTaskFactory;
	private RequestWorker worker;
	@Mock
	private PutGetServer server;
	@Mock
	private SocketChannel socket;
	@Mock
	private ServerDataEvent event;
	@Mock
	private CommunicationTask task;
	@Mock
	private Cpu cpu;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		when(task.getCpuSlot()).thenReturn(CPU_SLOT);
		when(plc.getCpu(CPU_SLOT)).thenReturn(cpu);
		when(communicationTaskFactory.createTask(event)).thenReturn(task);
		when(event.getServer()).thenReturn(server);

		worker = new RequestWorker(plc, communicationTaskFactory);
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
