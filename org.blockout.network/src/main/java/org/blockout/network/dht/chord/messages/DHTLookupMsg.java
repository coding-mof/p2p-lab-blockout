package org.blockout.network.dht.chord.messages;

import org.blockout.network.dht.IHash;
import org.blockout.network.message.IMessage;

public class DHTLookupMsg implements IMessage {
	private static final long serialVersionUID = 2598888869529765920L;
	private IHash hash;
	
	public DHTLookupMsg(IHash next) {
		this.hash = next;
	}
	
	public IHash getHash(){
		return this.hash;
	}

}
