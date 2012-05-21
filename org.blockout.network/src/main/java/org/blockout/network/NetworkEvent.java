package org.blockout.network;

import java.io.Serializable;
import java.net.SocketAddress;

public class NetworkEvent<T extends Serializable> implements INetworkEvent<T> {

	private T content;
	private SocketAddress localAddress;
	private SocketAddress remoteAddress;

	public NetworkEvent(SocketAddress remoteAddress,
			SocketAddress localAddress, Object message) {
		this.remoteAddress = remoteAddress;
		this.localAddress = localAddress;
		this.content = (T) message;
	}

	@Override
	public void setRecipient(NodeInfo recipient) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NodeInfo getRecipient() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInfo getSender() {
		return null;
	}

	@Override
	public T getContent() {
		return this.content;
	}

	@Override
	public void setContent(T event) {
		this.content = event;		
	}

}
