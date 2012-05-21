package org.blockout.network.dht;

import java.util.concurrent.Future;
import org.blockout.network.NodeInfo;

public interface IDistributedHashTable {
	public Future<NodeInfo> lookup(String hash);
}
