package de.peteral.softplc.comm.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("javadoc")
public class ClientChannelCacheTest {
	private static final int SLOT = 10;
	@Mock
	private SocketChannel socket;
	@Mock
	private SocketAddress address;

	@Before
	public void setup() throws IOException {
		MockitoAnnotations.initMocks(this);

		when(socket.getRemoteAddress()).thenReturn(address);

		ClientChannelCache.getInstance().clear();
	}

	@Test
	public void getInstance_None_ReturnsCacheInstance() {
		assertNotNull(ClientChannelCache.getInstance());
	}

	@Test
	public void getInstance_InvokedTwice_ReturnsSameInstance() {
		assertSame(ClientChannelCache.getInstance(),
				ClientChannelCache.getInstance());
	}

	@Test
	public void addChannel_AnyChannel_GetSlotReturnsCorrectSlot() {
		ClientChannelCache.getInstance().addChannel(socket, SLOT);

		assertEquals(SLOT,
				(int) ClientChannelCache.getInstance().getSlot(socket));
	}

	@Test
	public void getClientCount_OneChannel_ReturnsOne() {
		ClientChannelCache.getInstance().addChannel(socket, SLOT);

		assertEquals(1,
				ClientChannelCache.getInstance().getConnectionCount(SLOT));
	}

	@Test
	public void remove_RegisteredSocket_SocketRemoved() {
		ClientChannelCache.getInstance().addChannel(socket, SLOT);
		ClientChannelCache.getInstance().removeChannel(socket);

		assertNull(ClientChannelCache.getInstance().getSlot(socket));
	}
}
