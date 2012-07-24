package org.blockout.network.reworked;

import org.blockout.network.dht.IHash;
import org.jboss.netty.channel.Channel;

public class LookupResult {
	private final IHash		hash;
	private final Channel	channel;

	public LookupResult(final IHash hash, final Channel channel) {
		this.hash = hash;
		this.channel = channel;
	}

	public IHash getHash() {
		return hash;
	}

	public Channel getChannel() {
		return channel;
	}

}