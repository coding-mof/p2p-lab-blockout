package org.blockout.network;

import org.blockout.network.dht.IHash;

public class LocalNode {

	private static final Object	singletonLock	= new Object();
	private static IHash		localNodeId;

	private static IHash getLocalNodeId() {
		synchronized ( singletonLock ) {
			if ( localNodeId == null ) {
				localNodeId = null;
			}
		}
		return localNodeId;
	}

	public IHash getNodeId() {
		return getLocalNodeId();
	}
}
