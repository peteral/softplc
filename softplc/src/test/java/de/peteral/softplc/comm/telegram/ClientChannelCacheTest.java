package de.peteral.softplc.comm.telegram;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

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

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

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
	public void remove_RegisteredSocket_SocketRemoved() {
		ClientChannelCache.getInstance().addChannel(socket, SLOT);
		ClientChannelCache.getInstance().removeChannel(socket);

		assertNull(ClientChannelCache.getInstance().getSlot(socket));
	}
}
