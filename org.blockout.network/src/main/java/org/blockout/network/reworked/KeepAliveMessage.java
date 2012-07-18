package org.blockout.network.reworked;

import java.io.Serializable;

public class KeepAliveMessage implements Serializable {
	private final boolean	ack;

	public KeepAliveMessage(final boolean ack) {
		this.ack = ack;
	}

	public boolean isAck() {
		return ack;
	}
}
