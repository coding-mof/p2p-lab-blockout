package org.blockout.network.dht;

import java.util.Hashtable;

import org.jboss.netty.channel.Channel;

public interface IDistributedHashTable {
	public Channel connectTo(IHash nodeId, Hashtable<IHash, Channel> protoFingerTable);
}