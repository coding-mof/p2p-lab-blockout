package org.blockout.network.dht.chord.messages;

import org.blockout.network.dht.IHash;
import org.blockout.network.message.IMessage;

public class DHTJoin implements IMessage {

	private static final long	serialVersionUID	= -3826534127305770010L;
	private final IHash			origin;

	public DHTJoin(final IHash origin) {
		this.origin = origin;
	}

	public IHash getOrigin() {
		return origin;
	}

}
