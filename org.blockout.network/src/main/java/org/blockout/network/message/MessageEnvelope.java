package org.blockout.network.message;

import org.blockout.network.discovery.INodeAddress;

public class MessageEnvelope<T extends IMessage> implements IMessageEnvelope<T> {
	private static final long serialVersionUID = 1808833825449980446L;
	
	private final T msg;
	private final INodeAddress recipient;
	private INodeAddress sender;

	public MessageEnvelope(T msg, INodeAddress recipient, INodeAddress sender) {
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

	@Override
	public String toString() {
		return "Sender: " + this.getSender() + ", Receiver: "
				+ this.getRecipient() + ", Message: " + this.getMessage();
	}

	@Override
	public void setSender(INodeAddress remoteAddress) {
		this.sender = remoteAddress;
	}
}
