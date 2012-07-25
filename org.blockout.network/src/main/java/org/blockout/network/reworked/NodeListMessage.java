package org.blockout.network.reworked;

import java.util.Set;

import org.blockout.network.dht.IHash;

/**
 * Propagation message to inform peers about other nodes.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class NodeListMessage extends AbstractMessage {

	private static final long			serialVersionUID	= -3198973234063150048L;
	private final Set<HashAndAddress>	knownNodes;

	public NodeListMessage(final Set<HashAndAddress> nodes) {
		knownNodes = nodes;
	}

	public Set<HashAndAddress> getKnownNodes() {
		return knownNodes;
	}

	@Override
	public IHash getReceiver() {
		return null;
	}

	@Override
	public boolean isRoutable() {
		return false;
	}

	@Override
	public String toString() {
		return "NodeListMessage[nodes=" + knownNodes + "]";
	}
}
