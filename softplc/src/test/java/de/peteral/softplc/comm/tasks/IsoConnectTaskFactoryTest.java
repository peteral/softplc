package de.peteral.softplc.comm.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.channels.SocketChannel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.comm.common.ClientChannelCache;
import de.peteral.softplc.comm.common.ServerDataEvent;

@SuppressWarnings("javadoc")
public class IsoConnectTaskFactoryTest {
	private static final byte TYPE = 2;

	private static final byte SLOT = 5;

	private IsoConnectTaskFactory factory;
	@Mock
	private ServerDataEvent event;
	@Mock
	private ClientChannelCache channelCache;
	@Mock
	private CommunicationTaskFactory communicationTaskFactory;
	@Mock
	private SocketChannel socket;

	/* @formatter:off */
	private static final byte[] VALID_DATA = {
		0x03, 0x00, 0x00, 0x1A, // +0 RFC header
		0x15, (byte) 0xE0, 0x00, 0x00, 0x00, 0x01, 0x00, // +4 ISO header
		(byte) 0xC1, 0x02, 0x01, 0x00, // +11 source
		(byte) 0xC2, 0x02, TYPE, SLOT, // +15 destination
		(byte) 0xC0, 0x01, 0x09 // +19 TPUD
	};
	/* @formatter:off */
	private static final byte[] INVALID_DATA = {
		0x04, 0x00, 0x00, 0x1A, // +0 RFC header
		0x15, (byte) 0xE0, 0x00, 0x00, 0x00, 0x01, 0x00, // +4 ISO header
		(byte) 0xC1, 0x02, 0x01, 0x00, // +11 source
		(byte) 0xC2, 0x02, TYPE, SLOT, // +15 destination
		(byte) 0xC0, 0x01, 0x09 // +19 TPUD
	};

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		factory = new IsoConnectTaskFactory();

		ClientChannelCache.installMock(channelCache);
	}

	@After
	public void teardown() {
		ClientChannelCache.installMock(null);
	}

	@Test
	public void canHandle_ValidData_ReturnsTrue() {
		when(event.getData()).thenReturn(VALID_DATA);

		assertTrue(factory.canHandle(event));
	}

	@Test
	public void canHandle_InvalidData_ReturnsFalse() {
		when(event.getData()).thenReturn(INVALID_DATA);

		assertFalse(factory.canHandle(event));
	}

	@Test
	public void canHandle_DataTooShort_ReturnsFalse() {
		when(event.getData()).thenReturn(new byte[]{});

		assertFalse(factory.canHandle(event));
	}

	@Test
	public void createTask_ValidEvent_ReturnsCorrectTask() {
		when(event.getData()).thenReturn(VALID_DATA);

		IsoConnectTask task = (IsoConnectTask) factory.createTask(event, communicationTaskFactory);

		assertEquals(SLOT, task.getCpuSlot());
	}

	@Test
	public void createTask_ValidEvent_RegistersChannelForGivenSlot() {
		when(event.getData()).thenReturn(VALID_DATA);
		when(event.getSocket()).thenReturn(socket);

		factory.createTask(event, communicationTaskFactory);

		verify(channelCache).addChannel(socket, SLOT);
	}
}
