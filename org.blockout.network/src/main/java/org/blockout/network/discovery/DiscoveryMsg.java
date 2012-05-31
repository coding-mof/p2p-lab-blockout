package org.blockout.network.discovery;

import org.blockout.network.dht.IHash;
import org.blockout.network.message.IMessage;

public class DiscoveryMsg implements IMessage {

	private final int port;
	private final IHash nodeId;

	public DiscoveryMsg(int port, IHash nodeId) {
		this.port = port;
		this.nodeId = nodeId;
	}

	public int getPort() {
		return this.port;
	}

	public IHash getNodeId() {
		return this.nodeId;
	}

}
