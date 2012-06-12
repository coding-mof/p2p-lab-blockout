package org.blockout.world.messeges;

import java.io.Serializable;

import org.blockout.common.TileCoordinate;
import org.blockout.network.dht.Hash;
import org.blockout.network.dht.IHash;

/**
 * implementation of the {@link IComparator} interface
 * 
 * @author key3
 */
public class DefaultComparator implements IComparator, Serializable {
	
	private String hash;
	
	public DefaultComparator(IHash hash) {
		this.hash = hash.getValue();
	}

	/**
	 * Determines whether the given coordinate is still within
	 * the responsibility of the current manager
	 * 
	 * 
	 * @param coordinate
	 * @return	true if the Hash of the given coordinate is greater then the hash
	 * 			false otherwise
	 */
	@Override
	public boolean compare(TileCoordinate coordinate) {
		final IHash tmp = new Hash(coordinate);
		return hash.compareTo(tmp.getValue()) < 0;
	}

	@Override
	public void setHash(IHash hash) {
		this.hash = hash.getValue();
	}

}
