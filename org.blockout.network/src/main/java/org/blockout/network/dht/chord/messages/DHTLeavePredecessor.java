package org.blockout.network.dht.chord.messages;

import org.blockout.network.INodeAddress;
import org.blockout.network.message.IMessage;

public class DHTLeavePredecessor implements IMessage {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1671016595061074047L;
	private final INodeAddress	node;

	public DHTLeavePredecessor(final INodeAddress predecessor) {
		node = predecessor;
	}

	public INodeAddress getNode() {
		return node;
	}

}
