package org.blockout.network.message;

import java.io.Serializable;
import java.net.InetSocketAddress;

import org.blockout.network.dht.IHash;

public interface IMessageEnvelope<T extends IMessage> extends Serializable {
	public IHash getRecipient();

	public IHash getSender();

	public InetSocketAddress getReceiverAddress();

	public InetSocketAddress getSenderAddress();

	public T getMessage();

	public void setSender( IHash remoteAddress );
}
