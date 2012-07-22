package org.blockout.network.reworked;

import java.net.SocketAddress;

import org.blockout.network.dht.IHash;

public class JoinRequestMessage extends AbstractMessage {

	private static final long	serialVersionUID	= -1228630304072607020L;
	private final IHash			nodeId;
	private final SocketAddress	address;

	public JoinRequestMessage(final IHash nodeId, final SocketAddress address) {
		this.nodeId = nodeId;
		this.address = address;
	}

	public IHash getNodeId() {
		return nodeId;
	}

	public SocketAddress getAddress() {
		return address;
	}

	@Override
	public IHash getReceiver() {
		return getNodeId().getNext();
	}
}
