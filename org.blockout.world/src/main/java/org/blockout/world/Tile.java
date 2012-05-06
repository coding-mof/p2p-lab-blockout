package org.blockout.world;

import org.blockout.world.entity.Entity;

/**
 * A tile represents the smallest unit in the world's coordinate system.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class Tile {
	public static final int	GROUND_HEIGHT	= 0;
	public static final int	WALL_HEIGHT		= 3;

	protected int			tileType;
	protected int			height;
	protected Entity		object;

	public Tile(final int type) {
		tileType = type;
		height = GROUND_HEIGHT;
	}

	public Tile(final int type, final int height) {
		tileType = type;
		this.height = height;
	}

	public Tile(final int type, final Entity entity) {
		tileType = type;
		height = GROUND_HEIGHT;
		this.object = entity;
	}

	public int getTileType() {
		return tileType;
	}

	public void setTileType( final int tileType ) {
		this.tileType = tileType;
	}

	/**
	 * Returns the object which is on the tile if present; otherwise null.
	 * 
	 * @return The object which is on the tile if present; otherwise null.
	 */
	public Entity getEntityOnTile() {
		return object;
	}

	/**
	 * Returns the height of this tile. Don't mix this value with the actual
	 * tile size up. Although the world's coordinate system is 2 dimensional
	 * this value represents the height in the 3rd dimension.
	 * 
	 * @return
	 */
	public int getHeight() {
		return height;
	}
}
