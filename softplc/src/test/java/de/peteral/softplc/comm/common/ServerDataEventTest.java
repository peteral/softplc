package de.peteral.softplc.comm.common;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.nio.channels.SocketChannel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.model.NetworkInterface;

@SuppressWarnings("javadoc")
public class ServerDataEventTest {
	private static final byte[] DATA = new byte[] { 0x01, 0x02 };
	@Mock
	private NetworkInterface server;
	@Mock
	private SocketChannel socket;
	private ServerDataEvent event;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		event = new ServerDataEvent(server, socket, DATA);
	}

	@Test
	public void getServer_None_ReturnsServer() {
		assertEquals(server, event.getNetworkInterface());
	}

	@Test
	public void getSocket_None_ReturnsSocket() {
		assertEquals(socket, event.getSocket());
	}

	@Test
	public void getData_None_ReturnsData() {
		assertArrayEquals(DATA, event.getData());
	}
}
