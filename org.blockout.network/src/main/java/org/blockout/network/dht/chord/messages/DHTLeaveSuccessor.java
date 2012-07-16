package org.blockout.network.dht.chord.messages;

import org.blockout.network.dht.IHash;
import org.blockout.network.message.IMessage;

public class DHTLeaveSuccessor implements IMessage {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5472893514533929645L;

	private final IHash			node;

	public DHTLeaveSuccessor(final IHash successor) {
		node = successor;
	}

	public IHash getNode() {
		return node;
	}

}
