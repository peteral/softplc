package de.peteral.softplc.comm.tasks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.channels.SocketChannel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.model.PutGetServer;

@SuppressWarnings("javadoc")
public class IsoConnectTaskTest {
	private static final int SLOT = 4;
	@Mock
	private PutGetServer server;
	@Mock
	private SocketChannel socket;
	@Mock
	private CommunicationTaskFactory factory;
	private IsoConnectTask task;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		task = new IsoConnectTask(server, socket, SLOT, factory);
	}

	@Test
	public void isOk_NewInstance_ReturnsTrue() {
		assertTrue(task.isOk());
	}

	@Test
	public void invalidate_None_IsOkReturnsFalse() {
		task.invalidate();

		assertFalse(task.isOk());
	}

	@Test
	public void execute_None_ThrowsNoException() {
		task.execute(null);
	}
}
