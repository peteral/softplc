package de.peteral.softplc.comm;

import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.model.Plc;
import de.peteral.softplc.model.PutGetServerEvent;
import de.peteral.softplc.model.PutGetServerObserver;

@SuppressWarnings("javadoc")
public class PutGetServerImplTest {

	@Mock
	private Plc plc;
	private NetworkInterfaceImpl server;
	@Mock
	private PutGetServerObserver observer;
	@Mock
	private PutGetServerEvent event;

	@Before
	public void setup() throws IOException {
		MockitoAnnotations.initMocks(this);

		server = new NetworkInterfaceImpl();
	}

	@Test
	public void addObserver_AnObserver_ObserverInvokedWhenNotifyListenersIsCalled() {
		server.addObserver(observer);

		server.notifyObservers(event);

		verify(observer).onTelegram(event);
	}

	@Test
	public void addObserver_SameObserverTwice_ObserverInvokedOnceWhenNotifyListenersIsCalled() {
		server.addObserver(observer);
		server.addObserver(observer);

		server.notifyObservers(event);

		verify(observer).onTelegram(event);
	}

	@Test
	public void removeObserver_RegisteredObserver_ObserverNotInvokedWhenNotifyListenersIsCalled() {
		server.addObserver(observer);
		server.removeObserver(observer);

		server.notifyObservers(event);

		verify(observer, never()).onTelegram(event);
	}
}
