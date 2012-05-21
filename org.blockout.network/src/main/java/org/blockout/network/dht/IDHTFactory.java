package org.blockout.network.dht;

import org.blockout.network.server.IServer;

public interface IDHTFactory {
	public IDistributedHashTable createDHT(IServer server);
}
