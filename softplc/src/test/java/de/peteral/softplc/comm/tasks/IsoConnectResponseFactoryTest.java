package de.peteral.softplc.comm.tasks;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.model.CommunicationTask;

@SuppressWarnings("javadoc")
public class IsoConnectResponseFactoryTest {
	private IsoConnectResponseFactory factory;
	@Mock
	private IsoConnectTask task;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		factory = new IsoConnectResponseFactory();
	}

	@Test
	public void canHandle_IsoConnectTask_ReturnsTrue() {
		assertTrue(factory.canHandle(task));
	}

	@Test
	public void canHandle_NotIsoConnectTask_ReturnsFalse() {
		CommunicationTask otherTask = mock(CommunicationTask.class);
		assertFalse(factory.canHandle(otherTask));
	}

	@Test
	public void createResponse_TaskIsOk_ReturnsGoodResponse() {
		when(task.isOk()).thenReturn(true);
		assertArrayEquals(IsoConnectResponseFactory.GOOD_RESPONSE,
				factory.createResponse(task));
	}

	@Test
	public void createResponse_TaskIsNotOk_ReturnsBadResponse() {
		when(task.isOk()).thenReturn(false);
		assertArrayEquals(IsoConnectResponseFactory.BAD_RESPONSE,
				factory.createResponse(task));
	}
}
