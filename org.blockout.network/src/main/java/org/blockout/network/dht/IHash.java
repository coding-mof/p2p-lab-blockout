package org.blockout.network.dht;

import java.io.Serializable;

public interface IHash extends Serializable{
	public String getValue();
	public IHash getNext();
}
