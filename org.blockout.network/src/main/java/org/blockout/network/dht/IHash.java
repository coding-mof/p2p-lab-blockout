package org.blockout.network.dht;

import java.io.Serializable;
import java.math.BigInteger;

public interface IHash extends Serializable, Comparable<IHash> {
	public String getValue();

	public IHash getNext();

	public IHash getPrevious();

	public int getM();

	public IHash getClosest( IHash a, IHash b );

	public BigInteger getInteger();
}
