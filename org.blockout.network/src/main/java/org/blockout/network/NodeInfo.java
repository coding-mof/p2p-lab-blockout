package org.blockout.network;

import java.net.InetAddress;
import java.util.UUID;

/**
 * This class encapsulates information about a node in the network.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class NodeInfo {
	protected UUID			nodeId;
	protected InetAddress	inetAddress;

	public NodeInfo(final InetAddress inetAddress) {
		super();
		this.inetAddress = inetAddress;
	}

	public UUID getNodeId() {
		return nodeId;
	}

	public InetAddress getInetAddress() {
		return inetAddress;
	}

	@Override
	public String toString() {
		return "Node[address=" + inetAddress + "]";
	}
}
