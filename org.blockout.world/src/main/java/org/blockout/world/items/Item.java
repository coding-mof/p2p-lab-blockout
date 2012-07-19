package org.blockout.world.items;

import java.io.Serializable;

import org.blockout.engine.SpriteType;

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

	/**
	 * Returns the type of sprite used to render this item.
	 * 
	 * @return The type of sprite used to render this item.
	 */
	public SpriteType getSpriteType();

	public String getDescription();
}
