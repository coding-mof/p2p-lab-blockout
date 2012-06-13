package org.blockout.network.dht.chord.messages;

import org.blockout.network.INodeAddress;
import org.blockout.network.message.IMessage;

public class DHTLeaveSuccessor implements IMessage {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5472893514533929645L;

	private final INodeAddress	node;

	public DHTLeaveSuccessor(final INodeAddress successor) {
		node = successor;
	}

	public INodeAddress getNode() {
		return node;
	}

}
