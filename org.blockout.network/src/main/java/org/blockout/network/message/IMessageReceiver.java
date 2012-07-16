package org.blockout.network.message;

import org.blockout.network.dht.IHash;

public interface IMessageReceiver {
	public void receive( Object msg, IHash origin );
}
