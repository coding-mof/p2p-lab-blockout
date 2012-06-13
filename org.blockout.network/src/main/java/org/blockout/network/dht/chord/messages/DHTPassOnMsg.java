package org.blockout.network.dht.chord.messages;

import org.blockout.network.dht.IHash;
import org.blockout.network.message.IMessage;

public class DHTPassOnMsg implements IMessage {
	private static final long serialVersionUID = 0;
	private IMessage msg;
	private IHash target;

	public DHTPassOnMsg(IMessage msg, IHash nodeId) {
		this.msg = msg;
		this.target = nodeId;
	}

	public IMessage getMessage() {
		return this.msg;
	}

	public IHash getReceiver() {
		return this.target;
	}
}
