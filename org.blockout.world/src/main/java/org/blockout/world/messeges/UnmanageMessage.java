package org.blockout.world.messeges;

import org.blockout.common.TileCoordinate;
import org.blockout.network.dht.IHash;
import org.blockout.network.message.IMessage;

public class UnmanageMessage implements IMessage {
	
	private static final long serialVersionUID = 6901320317556924447L;
	
	private IComparator comparator;
	private IHash hash;
	
	/**
	 * 
	 * @param hash			the hash to whichthe {@link TileCoordinate}'s should be compared
	 * @param comparator	the {@link IComparator} that should be used for comparing
	 */
	public UnmanageMessage(IHash hash, IComparator comparator) {
		this.comparator = comparator;
		this.hash = hash;
		comparator.setHash(hash);
	}

	public IComparator getComparator() {
		return comparator;
	}

	public IHash getHash() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnmanageMessage other = (UnmanageMessage) obj;
		if (comparator == null) {
			if (other.comparator != null)
				return false;
		} else if (!comparator.equals(other.comparator))
			return false;
		if (hash == null) {
			if (other.hash != null)
				return false;
		} else if (!hash.equals(other.hash))
			return false;
		return true;
	}
	
	
	
}
