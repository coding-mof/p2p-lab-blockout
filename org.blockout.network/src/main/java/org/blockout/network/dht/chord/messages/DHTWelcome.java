package org.blockout.network.dht.chord.messages;

import org.blockout.network.dht.IHash;
import org.blockout.network.message.IMessage;

public class DHTWelcome implements IMessage {

	private static final long	serialVersionUID	= -1442230602052840020L;
	private final IHash			predecessor;

	public DHTWelcome(final IHash predecessor) {
		this.predecessor = predecessor;
	}

	public IHash getPredecessor() {
		return predecessor;
	}
}
