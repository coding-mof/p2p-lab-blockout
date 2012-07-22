package org.blockout.network.reworked;

import org.blockout.network.dht.IHash;

public class KeepAliveMessage extends AbstractMessage {
	private static final long	serialVersionUID	= -2804957469122261061L;
	private final boolean		ack;

	public KeepAliveMessage(final boolean ack) {
		this.ack = ack;
	}

	public boolean isAck() {
		return ack;
	}

	@Override
	public IHash getReceiver() {
		return null;
	}

	@Override
	public boolean isRoutable() {
		return false;
	}
}
