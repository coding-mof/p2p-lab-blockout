package org.blockout.network.dht.chord.messages;

import org.blockout.network.dht.IHash;
import org.blockout.network.message.IMessage;

public class DHTLeavePredecessor implements IMessage {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1671016595061074047L;
	private final IHash			node;

	public DHTLeavePredecessor(final IHash predecessor) {
		node = predecessor;
	}

	public IHash getNode() {
		return node;
	}

}
