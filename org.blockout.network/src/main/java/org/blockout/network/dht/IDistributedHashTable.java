package org.blockout.network.dht;

import org.blockout.network.message.IMessage;

public interface IDistributedHashTable {
	public void sendTo(IMessage msg, IHash nodeId);
}