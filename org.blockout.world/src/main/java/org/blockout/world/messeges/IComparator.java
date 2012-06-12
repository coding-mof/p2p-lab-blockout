package org.blockout.world.messeges;

import org.blockout.common.TileCoordinate;
import org.blockout.network.dht.IHash;



/**
 * Interface for comparing {@link TileCoordinate}'s to each other
 * to determine whether the Component with the given {@link TileCoordinate}
 * is still within the responsibility of the Manager 
 * 
 * @author key3
 *
 */
public interface IComparator {

	/**
	 * Determines whether the given coordinate is still within
	 * the responsibility of the current manager
	 * 
	 * @param coordinate
	 * @return	true if the given coordinate is still within the own responsibility
	 * 			false otherwise
	 */
	public boolean compare(TileCoordinate coordinate);
	
	/**
	 * sets the hash to which the {@link TileCoordinate}s should be compared
	 * 
	 * @param hash
	 */
	public void setHash(IHash hash);
}
