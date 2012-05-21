package org.blockout.world.items;

import java.io.Serializable;

/**
 * Common interface for all items a player can carry.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public interface Item extends Serializable {
	/**
	 * Returns a human readable name of the item.
	 * 
	 * @return A human readable name of the item.
	 */
	public String getName();
}
