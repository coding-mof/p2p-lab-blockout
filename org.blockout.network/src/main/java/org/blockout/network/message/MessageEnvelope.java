package org.blockout.network.message;

import org.blockout.network.INodeAddress;

public class MessageEnvelope<T extends IMessage> implements IMessageEnvelope<T> {
	private static final long	serialVersionUID	= 1808833825449980446L;

	private final T				msg;
	private final INodeAddress	recipient;
	private INodeAddress		sender;

	public MessageEnvelope(final T msg, final INodeAddress recipient, final INodeAddress sender) {
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
		return "Message: " + this.getMessage() + ", Sender: " + this.getSender() + ", Receiver: " + this.getRecipient();
	}

	@Override
	public void setSender( final INodeAddress remoteAddress ) {
		this.sender = remoteAddress;
	}
}
