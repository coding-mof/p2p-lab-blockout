package org.blockout.network.reworked;

import java.io.Serializable;

public class KeepAliveMessage implements Serializable {
	private static final long	serialVersionUID	= -2804957469122261061L;
	private final boolean		ack;

	public KeepAliveMessage(final boolean ack) {
		this.ack = ack;
	}

	public boolean isAck() {
		return ack;
	}
}
