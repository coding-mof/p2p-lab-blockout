package org.blockout.network.reworked;

import java.io.Serializable;

import org.blockout.network.dht.IHash;

public class ChordEnvelope implements Serializable {

	private final IHash			senderId;
	private final IHash			receiverId;
	private final Serializable	content;

	public ChordEnvelope(final IHash senderId, final IHash receiverId, final Serializable content) {
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.content = content;
	}

	public IHash getSenderId() {
		return senderId;
	}

	public IHash getReceiverId() {
		return receiverId;
	}

	public Serializable getContent() {
		return content;
	}
}
