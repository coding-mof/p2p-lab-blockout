package org.blockout.network.reworked;

import java.net.SocketAddress;

import org.blockout.network.dht.IHash;

import com.google.common.base.Preconditions;

/**
 * The join message is sent by new nodes when they enter the chord ring. As part
 * of the protocol only the TCP client sends the join message. The message then
 * gets routed through the ring until it reaches the node that is responsible
 * for the new node's id. This node will then respond with a
 * {@link WelcomeMessage}.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class IAmMessage extends AbstractMessage {

	private static final long	serialVersionUID	= -7039915306236068931L;
	private final IHash			nodeId;
	private final SocketAddress	address;

	public IAmMessage(final IHash nodeId, final SocketAddress address) {

		Preconditions.checkNotNull( nodeId );
		Preconditions.checkNotNull( address );

		this.nodeId = nodeId;
		this.address = address;
	}

	/**
	 * The id of the new node that wants to enter the ring.
	 * 
	 * @return
	 */
	public IHash getNodeId() {
		return nodeId;
	}

	/**
	 * The {@link SocketAddress} of the new node's server socket that accepts
	 * TCP client connections. This address is used by existing nodes in the
	 * ring to connect to the new node and send the {@link WelcomeMessage}.
	 * 
	 * @return
	 */
	public SocketAddress getAddress() {
		return address;
	}

	@Override
	public IHash getReceiver() {
		return getNodeId().getNext();
	}

	@Override
	public boolean isRoutable() {
		return false;
	}
}
