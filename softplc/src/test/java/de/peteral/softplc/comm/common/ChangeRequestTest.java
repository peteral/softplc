package de.peteral.softplc.comm.common;

import static org.junit.Assert.assertEquals;

import java.nio.channels.SocketChannel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("javadoc")
public class ChangeRequestTest {
	private static final int TYPE = 10;
	private static final int OPS = 20;
	@Mock
	private SocketChannel socket;
	private ChangeRequest request;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		request = new ChangeRequest(socket, TYPE, OPS);
	}

	@Test
	public void getSocket_None_ReturnsSocket() {
		assertEquals(socket, request.getSocket());
	}

	@Test
	public void getType_None_ReturnsType() {
		assertEquals(TYPE, request.getType());
	}

	@Test
	public void getOps_None_ReturnsOps() {
		assertEquals(OPS, request.getOps());
	}
}
