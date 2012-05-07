package org.blockout.world;

import org.blockout.common.TileCoordinate;

import com.google.common.base.Preconditions;

/**
 * 
 * @author Konstantin Ramig
 */
public class Chunk {

	public static final int			CHUNK_SIZE	= 48;

	private final TileCoordinate	pos;
	private Tile[][]				tiles;

	/**
	 * 
	 * @param x
	 *            x coordinate of the Chunk in the World
	 * @param y
	 *            y coordinate of the Chunk in the World
	 * @param tiles
	 *            tiles contained by the chunk should be an NxN matrix or null
	 * @throws IllegalArgumentException
	 *             if tiles is not a NxN matrix or null
	 */
	public Chunk(final int x, final int y, final Tile[][] tiles) throws IllegalArgumentException {
		setTiles( tiles );
		pos = new TileCoordinate( x, y );
	}

	/**
	 * 
	 * @param position
	 *            position of the Chunk in the World
	 * @param tiles
	 *            tiles contained by the chunk should be an NxN matrix or null
	 * @throws IllegalArgumentException
	 *             if tiles is not a NxN matrix or null
	 */
	public Chunk(final TileCoordinate position, final Tile[][] tiles) throws IllegalArgumentException {
		setTiles( tiles );
		pos = position;
	}

	/**
	 * @param x
	 *            coordinate of the Chunk in the World
	 */
	public int getX() {
		return pos.getX();
	}

	/**
	 * @param x
	 *            coordinate of the Chunk in the World
	 */
	public int getY() {
		return pos.getY();
	}

	/**
	 * @return the position of the Chunk in the world
	 */
	public TileCoordinate getPosition() {
		return pos;
	}

	/**
	 * @param tiles
	 *            tiles contained by the chunk should be an NxN matrix or null
	 * @throws NullPointerException
	 *             If you pass in null.
	 * @throws IllegalArgumentException
	 *             If the given array/matrix is not symmetric.
	 */
	public void setTiles( final Tile[][] tiles ) {
		Preconditions.checkNotNull( tiles );
		if ( tiles.length != CHUNK_SIZE ) {
			throw new IllegalArgumentException( "Tile matrix is not symetric" );
		}
		for ( Tile[] tile : tiles ) {
			if ( tile.length != CHUNK_SIZE ) {
				throw new IllegalArgumentException( "Tile matrix is not symetric" );
			}
		}
		this.tiles = tiles;
	}

	/**
	 * 
	 * @param x
	 *            location of the Tile within the chunk
	 * @param y
	 *            location of the Tile within the chunk
	 * @return the Tile t the given location
	 * @throws NullPointerException
	 *             if tiles are still null
	 */
	public Tile getTile( final int x, final int y ) {
		int x2 = (x < 0) ? CHUNK_SIZE + x : x;
		int y2 = (x < 0) ? CHUNK_SIZE + y : y;
		return tiles[x2][y2];
	}

}