package org.blockout.network;

import java.util.UUID;

import org.blockout.network.dht.Hash;
import org.blockout.network.dht.IHash;

public class LocalNode {

	private final IHash	localNodeId;

	public LocalNode() {
		localNodeId = new Hash( UUID.randomUUID() );
	}

	// private static final Object singletonLock = new Object();
	// private static IHash localNodeId;
	//
	// private static IHash getLocalNodeId() {
	// synchronized ( singletonLock ) {
	// if ( localNodeId == null ) {
	// localNodeId = new Hash( UUID.randomUUID() );
	// }
	// }
	// return localNodeId;
	// }
	//
	// public IHash getNodeId() {
	// return getLocalNodeId();
	// }

	public IHash getNodeId() {
		return localNodeId; // return getLocalNodeId();
	}
}
