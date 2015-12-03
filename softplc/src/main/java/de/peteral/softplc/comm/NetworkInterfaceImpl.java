package de.peteral.softplc.comm;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.peteral.softplc.comm.common.SelectorThread;
import de.peteral.softplc.comm.tasks.CommunicationTaskFactory;
import de.peteral.softplc.model.NetworkInterface;
import de.peteral.softplc.model.Plc;
import de.peteral.softplc.model.PutGetServerEvent;
import de.peteral.softplc.model.PutGetServerObserver;

/**
 * Default PUT/GET server implementation.
 *
 * @author peteral
 */
// TODO Java NIO uses final methods which makes mocking impossible, use loopback
// socket for testing
public class NetworkInterfaceImpl implements NetworkInterface {
	private static final Logger LOGGER = Logger.getLogger("communication");

	private final List<PutGetServerObserver> observers = new ArrayList<>();
	private Map<Integer, SelectorThread> selectorThreads;

	/**
	 * Creates a new instance.
	 *
	 * @param port
	 */
	public NetworkInterfaceImpl() {
	}

	@Override
	public void start(Plc plc) throws IOException {
		// new threads need to be created every time
		selectorThreads = new ConcurrentHashMap<>();

		for (Integer port : CommunicationTaskFactory.getFactory().getAllPorts()) {
			selectorThreads.put(port, new SelectorThread(this, port));
		}

		for (SelectorThread thread : selectorThreads.values()) {
			thread.start(plc);
		}
	}

	@Override
	public void stop() throws IOException {
		// stop all selector threads
	}

	@Override
	public void send(SocketChannel socket, byte[] data) {
		try {
			InetSocketAddress address = (InetSocketAddress) socket.getLocalAddress();

			selectorThreads.get(address.getPort()).send(socket, data);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Failed retrieving local port number.", e);
		}
	}

	@Override
	public void addObserver(PutGetServerObserver o) {
		if (!observers.contains(o)) {
			observers.add(o);
		}
	}

	@Override
	public void removeObserver(PutGetServerObserver o) {
		observers.remove(o);
	}

	@Override
	public void notifyObservers(PutGetServerEvent event) {
		for (PutGetServerObserver observer : observers) {
			observer.onTelegram(event);
		}
	}

}
