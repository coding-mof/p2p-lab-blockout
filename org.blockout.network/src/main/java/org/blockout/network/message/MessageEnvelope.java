package org.blockout.network.message;

import java.net.InetSocketAddress;

import org.blockout.network.dht.IHash;

public class MessageEnvelope<T extends IMessage> implements IMessageEnvelope<T> {
	private static final long	serialVersionUID	= 1808833825449980446L;

	private final T				msg;
	private final IHash			recipient;
	private IHash				sender;

	public MessageEnvelope(final T msg, final IHash recipient, final IHash sender) {
		this.msg = msg;
		this.recipient = recipient;
		this.sender = sender;
	}

	@Override
	public IHash getSender() {
		return this.sender;
	}

	@Override
	public T getMessage() {
		return this.msg;
	}

	@Override
	public IHash getRecipient() {
		return this.recipient;
	}

	@Override
	public String toString() {
		return "Message: " + this.getMessage() + ", Sender: " + this.getSender() + ", Receiver: " + this.getRecipient();
	}

	@Override
	public void setSender( final IHash remoteAddress ) {
		this.sender = remoteAddress;
	}

	@Override
	public InetSocketAddress getReceiverAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InetSocketAddress getSenderAddress() {
		// TODO Auto-generated method stub
		return null;
	}
}
