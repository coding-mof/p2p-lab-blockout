package org.blockout.network.dht.chord.messages;

import org.blockout.network.INodeAddress;
import org.blockout.network.dht.IHash;
import org.blockout.network.message.IMessage;

public class DHTJoin implements IMessage {

	private static final long serialVersionUID = -3826534127305770010L;
	private INodeAddress origin;
	private IHash nodeId;

	public DHTJoin(INodeAddress origin, IHash nodeId) {
		this.origin = origin;
		this.nodeId = nodeId;
	}

	public IHash getNodeId() {
		return this.nodeId;
	}

	public INodeAddress getOrigin() {
		return this.origin;
	}

}
