package org.blockout.world;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Entity;
import org.blockout.world.entity.Player;

/**
 * Interface for world implementations. Theoretically the world has no bounds
 * but we're limited by <code>Integer.MIN_VALUE + 1</code> and
 * <code>Integer.MAX_VALUE</code> value of an integer. The reason for limiting
 * the min value at <code>Integer.MIN_VALUE + 1</code> is that it's not possible
 * to compute an absolute value of it (see {@link Math#abs(int)}).
 * 
 * @author Marc-Christian Schulze
 * 
 */
public interface IWorld {

	/**
	 * Returns the tile at the given location.
	 * 
	 * @param x
	 *            The x coordinate of the desired tile.
	 * @param y
	 *            The y coordinate of the desired tile.
	 * @return The tile at the given location or null if currently not present.
	 */
	public Tile getTile( int x, int y );

	/**
	 * Returns the position of the tile containing the given entity
	 * 
	 * @param entity
	 * @return Tupel with the coordinates of the tile in the world containing
	 *         the entity null if the current Chunk doesn't contain such an
	 *         entity
	 */
	public TileCoordinate findTile( Entity entity );

	/**
	 * Sets the position of the given player to the given coordinates
	 * should only used when moving the local player
	 * calling this method could result in moving the current view on the world
	 * 
	 * @param p the player which should be moved
	 * @param coord the new coordinates of he player
	 */
	public void setPlayerPosition( Player p, TileCoordinate coord );
	
	/**
	 * Sets the position of the given entity to the given coordinates
	 * should not be used when moving the local player
	 * 
	 * if the given entity is not found within the current world 
	 * 	it will be created at the given coordinates
	 * if the given coordinates are not found within the current world
	 * 	the given entity will be removed from the world
	 * else the given entity will be moved from its original position
	 * 	to the given coordinates
	 *  
	 * 
	 * @param e the entity which should be moved
	 * @param coord the new coordinates of he player
	 */
	public void setEnityPosition( Entity e, TileCoordinate coord );
	
	/**
	 *	Removes the given entity from the World
	 *	no actions will be taken i the given entity is 
	 *	not found within the world
	 *
	 * @param e the entity which should be moved
	 */
	public void removeEntity( Entity e);
}
