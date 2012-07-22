package org.blockout.network.reworked;

import java.io.Serializable;

import org.blockout.network.dht.IHash;

public abstract class AbstractMessage implements Serializable {

	public boolean isRoutable() {
		return true;
	}

	/**
	 * Returns the receiver hash or null if this message is not routable.
	 * 
	 * @return
	 */
	public abstract IHash getReceiver();
}
