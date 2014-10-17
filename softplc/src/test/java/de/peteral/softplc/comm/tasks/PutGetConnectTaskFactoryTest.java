package de.peteral.softplc.comm.tasks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.comm.common.ServerDataEvent;

@SuppressWarnings("javadoc")
public class PutGetConnectTaskFactoryTest {
	private static final byte[] INVALID_DATA = { 0x32, 0x05, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x08, 0x00, 0x00, (byte) 0xf0, 0x00, 0x00, 0x01, 0x00,
		0x01, 0x01, (byte) 0xe0 };

	private PutGetConnectTaskFactory factory;
	@Mock
	private ServerDataEvent event;
	@Mock
	private CommunicationTaskFactory taskFactory;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		factory = new PutGetConnectTaskFactory();
	}

	@Test
	public void canHandle_DataTooShort_ReturnsFalse() {
		when(event.getData()).thenReturn(new byte[] {});

		assertFalse(factory.canHandle(event));
	}

	@Test
	public void canHandle_CorrectData_ReturnsTrue() {
		when(event.getData()).thenReturn(PutGetConnectTaskFactory.DATA);

		assertTrue(factory.canHandle(event));
	}

	@Test
	public void canHandle_InvalidData_ReturnsFalse() {
		when(event.getData()).thenReturn(INVALID_DATA);

		assertFalse(factory.canHandle(event));
	}

	@Test
	public void createTask_Any_ReturnsInstanceOfPutGetConnectTask() {
		assertTrue(factory.createTask(event, taskFactory) instanceof PutGetConnectTask);
	}
}
