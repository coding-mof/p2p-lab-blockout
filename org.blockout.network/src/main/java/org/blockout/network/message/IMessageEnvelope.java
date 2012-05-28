package org.blockout.network.message;

import java.io.Serializable;

import org.blockout.network.INodeAddress;

public interface IMessageEnvelope<T extends IMessage> extends Serializable {
	public INodeAddress getRecipient();
	public INodeAddress getSender();
	public T getMessage();

	public void setSender(INodeAddress remoteAddress);
}
