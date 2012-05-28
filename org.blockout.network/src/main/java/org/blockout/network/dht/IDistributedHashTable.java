package org.blockout.network.dht;

import org.jboss.netty.channel.Channel;

public interface IDistributedHashTable {
	public Channel connectTo(IHash nodeId);
}