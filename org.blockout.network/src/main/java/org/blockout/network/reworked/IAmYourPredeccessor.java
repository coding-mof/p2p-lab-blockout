package org.blockout.network.reworked;

import org.blockout.network.dht.IHash;

import com.google.common.base.Preconditions;

public class IAmYourPredeccessor extends AbstractMessage {
	private static final long	serialVersionUID	= -3266094817926621826L;
	private final IHash			nodeId;

	public IAmYourPredeccessor(final IHash nodeId) {

		Preconditions.checkNotNull( nodeId );

		this.nodeId = nodeId;
	}

	public IHash getNodeId() {
		return nodeId;
	}

	@Override
	public IHash getReceiver() {
		return null;
	}

	@Override
	public boolean isRoutable() {
		return false;
	}
}
