package org.blockout.network.discovery;

import java.net.SocketAddress;

import org.blockout.network.message.IMessage;

public class DiscoveryMsg implements IMessage {

	private static final long	serialVersionUID	= -2443324053955389106L;
	private final SocketAddress	serverAddress;

	public DiscoveryMsg(final SocketAddress nodeInfo) {
		serverAddress = nodeInfo;
	}

	public SocketAddress getServerAddress() {
		return serverAddress;
	}

	@Override
	public String toString() {
		return "DiscoveryMsg[address=" + getServerAddress() + "]";
	}
}
