package org.blockout.world;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Entity;

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
	 * @param entity 
	 * @return Tupel with the coordinates of the tile in the world containing the entity
	 * 			null if the current Chunk doesn't contain such an entity
	 */
	public TileCoordinate findTile(Entity entity);
}
