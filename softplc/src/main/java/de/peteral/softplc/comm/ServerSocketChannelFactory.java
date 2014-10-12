package de.peteral.softplc.comm;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

/**
 * Wraps ServerSocketChannel.open() into a factory for unit testing purpose.
 *
 * @author peteral
 *
 */
public class ServerSocketChannelFactory {

	/**
	 * Wraps ServerSocketChannel.open() into a factory for unit testing purpose.
	 *
	 * @return new {@link ServerSocketChannel}
	 * @throws IOException
	 */
	public ServerSocketChannel open() throws IOException {
		return ServerSocketChannel.open();
	}
}
