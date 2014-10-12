package de.peteral.softplc.comm;

import java.nio.channels.SocketChannel;

class ServerDataEvent {
	public PutGetServerImpl server;
	public SocketChannel socket;
	public byte[] data;

	public ServerDataEvent(PutGetServerImpl server, SocketChannel socket,
			byte[] data) {
		this.server = server;
		this.socket = socket;
		this.data = data;
	}
}