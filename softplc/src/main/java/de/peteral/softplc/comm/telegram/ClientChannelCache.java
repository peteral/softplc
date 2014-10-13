package de.peteral.softplc.comm.telegram;

import java.nio.channels.SocketChannel;

public class ClientChannelCache {

	public static ClientChannelCache getInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getSlot(SocketChannel socket) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void addChannel(SocketChannel socket, int slot) {
		// TODO Auto-generated method stub

	}

	public void removeSocket(SocketChannel socket) {
		// TODO this must be invoked when a client socket channel is closed
	}
}
