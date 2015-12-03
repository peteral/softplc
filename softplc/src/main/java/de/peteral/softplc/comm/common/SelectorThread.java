package de.peteral.softplc.comm.common;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.peteral.softplc.comm.RequestWorker;
import de.peteral.softplc.model.NetworkInterface;
import de.peteral.softplc.model.Plc;

public class SelectorThread extends Thread {
	private static final Logger LOGGER = Logger.getLogger("communication");
	// The buffer into which we'll read data when it's available
	private final ByteBuffer readBuffer = ByteBuffer.allocate(8192);
	// Maps a SocketChannel to a list of ByteBuffer instances
	private final Map<SocketChannel, List<ByteBuffer>> pendingData = new HashMap<>();

	private boolean running;
	private Selector selector;

	// A list of PendingChange instances
	private final List<ChangeRequest> pendingChanges = new LinkedList<>();
	private RequestWorker worker;
	private NetworkInterface networkInterface;
	private int port;
	private Thread workerThread;
	// The channel on which we'll accept connections
	private ServerSocketChannel serverChannel;

	public SelectorThread(NetworkInterface networkInterface, int port) {
		this.networkInterface = networkInterface;
		this.port = port;
	}

	public void start(Plc plc) throws IOException {
		selector = initSelector(port);
		worker = new RequestWorker(plc);

		workerThread = new Thread(worker);
		workerThread.start();
		super.start();
	}

	public void stopThread() throws IOException {
		selector.close();
		serverChannel.close();

		running = false;
		worker.cancel();
	}

	private Selector initSelector(int port) throws IOException {
		// Create a new selector
		Selector socketSelector = SelectorProvider.provider().openSelector();

		// Create a new non-blocking server socket channel
		this.serverChannel = SelectorProvider.provider().openServerSocketChannel();
		serverChannel.configureBlocking(false);

		// Bind the server socket to the specified address and port
		InetSocketAddress isa = new InetSocketAddress(port);
		LOGGER.log(Level.INFO, "Open port " + port);
		serverChannel.socket().bind(isa);

		// Register the server socket channel, indicating an interest in
		// accepting new connections
		serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);

		return socketSelector;
	}

	@Override
	public void run() {
		running = true;

		while (running) {
			try {
				// Process any pending changes
				synchronized (this.pendingChanges) {
					Iterator<ChangeRequest> changes = this.pendingChanges.iterator();
					while (changes.hasNext()) {
						ChangeRequest change = changes.next();
						switch (change.getType()) {
						case ChangeRequest.CHANGEOPS:
							SelectionKey key = change.getSocket().keyFor(this.selector);
							if (key != null) {
								try {
									key.interestOps(change.getOps());
								} catch (CancelledKeyException e) {
									LOGGER.log(Level.INFO,
											"key cancelled in run of class " + getClass()
													.getName() + ", Selector= "
											+ ((this.selector == null) ? "null" : this.selector.getClass().getName()));
								}
							} else {
								LOGGER.log(Level.INFO,
										"key is null in run of class " + getClass()
												.getName() + ", Selector= "
										+ ((this.selector == null) ? "null" : this.selector.getClass().getName()));
							}
						}
					}
					this.pendingChanges.clear();
				}

				// Wait for an event one of the registered channels
				this.selector.select();

				// Iterate over the set of keys for which events are available
				Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();
				while (selectedKeys.hasNext()) {
					SelectionKey key = selectedKeys.next();
					selectedKeys.remove();

					if (!key.isValid()) {
						continue;
					}

					// Check what event is available and deal with it
					if (key.isAcceptable()) {
						this.accept(key);
					} else if (key.isReadable()) {
						this.read(key);
					} else if (key.isWritable()) {
						this.write(key);
					}
				}
			} catch (Exception e) {
				// no handling necessary, only gets here when socket address is
				// invalid
			}
		}
	}

	private void accept(SelectionKey key) throws IOException {
		// For an accept to be pending the channel must be a server socket
		// channel.
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

		// Accept the connection and make it non-blocking
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);

		// Register the new SocketChannel with our Selector, indicating
		// we'd like to be notified when there's data waiting to be read
		socketChannel.register(this.selector, SelectionKey.OP_READ);
	}

	private void read(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		// Clear out our read buffer so it's ready for new data
		this.readBuffer.clear();

		// Attempt to read off the channel
		int numRead;
		try {
			numRead = socketChannel.read(this.readBuffer);
		} catch (IOException e) {
			// The remote forcibly closed the connection, cancel
			// the selection key and close the channel.
			ClientChannelCache.getInstance().removeChannel(socketChannel);
			key.cancel();
			socketChannel.close();
			return;
		}

		if (numRead == -1) {
			// Remote entity shut the socket down cleanly. Do the
			// same from our end and cancel the channel.
			ClientChannelCache.getInstance().removeChannel(socketChannel);
			key.channel().close();
			key.cancel();
			return;
		}

		// Hand the data off to our worker thread

		byte[] dataCopy = new byte[numRead];
		System.arraycopy(this.readBuffer.array(), 0, dataCopy, 0, numRead);
		worker.processData(new ServerDataEvent(networkInterface, socketChannel, dataCopy));
	}

	private void write(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		synchronized (this.pendingData) {
			List<ByteBuffer> queue = this.pendingData.get(socketChannel);

			// Write until there's not more data ...
			while (!queue.isEmpty()) {
				ByteBuffer buf = queue.get(0);
				socketChannel.write(buf);
				if (buf.remaining() > 0) {
					// ... or the socket's buffer fills up
					break;
				}
				queue.remove(0);
			}

			if (queue.isEmpty()) {
				// We wrote away all data, so we're no longer interested
				// in writing on this socket. Switch back to waiting for
				// data.
				key.interestOps(SelectionKey.OP_READ);
			}
		}
	}

	/**
	 * Sends data back to client.
	 * <p>
	 *
	 * @param socket
	 * @param data
	 */
	public void send(SocketChannel socket, byte[] data) {
		synchronized (this.pendingChanges) {
			// Indicate we want the interest ops set changed
			this.pendingChanges.add(new ChangeRequest(socket, ChangeRequest.CHANGEOPS, SelectionKey.OP_WRITE));

			// And queue the data we want written
			synchronized (this.pendingData) {
				List<ByteBuffer> queue = this.pendingData.get(socket);
				if (queue == null) {
					queue = new ArrayList<>();
					this.pendingData.put(socket, queue);
				}
				queue.add(ByteBuffer.wrap(data));
			}
		}

		// Finally, wake up our selecting thread so it can make the required
		// changes
		this.selector.wakeup();
	}

}
