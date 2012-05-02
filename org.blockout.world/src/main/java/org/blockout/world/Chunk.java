package org.blockout.world;

import com.google.common.base.Preconditions;

/**
 * 
 * @author Konstantin Ramig
 */
public class Chunk {

	private static final int	CHUNK_SIZE	= 16;

	private int					x;
	private int					y;
	private Tile[][]			tiles;

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
		setX( x );
		setY( y );
	}

	/**
	 * @param x
	 *            coordinate of the Chunk in the World
	 */
	public void setX( final int x ) {
		this.x = x;
	}

	/**
	 * @param x
	 *            coordinate of the Chunk in the World
	 */
	public void setY( final int y ) {
		this.y = y;
	}

	/**
	 * @param x
	 *            coordinate of the Chunk in the World
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x
	 *            coordinate of the Chunk in the World
	 */
	public int getY() {
		return y;
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
		return tiles[x][y];
	}

}