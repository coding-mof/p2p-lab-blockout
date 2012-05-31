package org.blockout.network.dht.chord;

import org.blockout.network.INodeAddress;
import org.blockout.network.message.IMessage;

public class DHTWelcome implements IMessage {

	private static final long serialVersionUID = -1442230602052840020L;
	private INodeAddress predecessor;

	public DHTWelcome(INodeAddress predecessor) {
		this.predecessor = predecessor;
	}

	public INodeAddress getPredecessor() {
		return this.predecessor;
	}
}
