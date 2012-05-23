package org.blockout.network.dht;

import java.util.Hashtable;

import org.blockout.network.INodeAddress;
import org.blockout.network.message.IMessagePassing;
import org.jboss.netty.channel.Channel;

public interface IDistributedHashTable {
	public Channel connectTo(IHash nodeId, Hashtable<IHash, Channel> protoFingerTable);

	public void setUp(IMessagePassing messagePassing, INodeAddress nodeAddress);
}