package org.blockout.network.message;

import org.blockout.network.INodeAddress;

public class MessageEnvelope<T extends IMessage> implements IMessageEnvelope<T> {
	private static final long serialVersionUID = 1808833825449980446L;
	
	private T msg;
	private INodeAddress recipient;
	private INodeAddress sender;

	public MessageEnvelope(T msg, INodeAddress sender, INodeAddress recipient) {
		this.msg = msg;
		this.recipient = recipient;
		this.sender = sender;
	}

	@Override
	public INodeAddress getSender() {
		return this.sender;
	}

	@Override
	public T getMessage() {
		return this.msg;
	}

	@Override
	public INodeAddress getRecipient() {
		return this.recipient;
	}

}
