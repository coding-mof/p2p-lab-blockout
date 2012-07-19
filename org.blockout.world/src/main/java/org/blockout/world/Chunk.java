package org.blockout.world;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Entity;

import com.google.common.base.Preconditions;

/**
 * 
 * @author Konstantin Ramig
 */
public class Chunk implements Serializable {

	private static final long						serialVersionUID	= 6458398672895072597L;

	public static final int							CHUNK_SIZE			= 48;

	private final HashMap<Entity, TileCoordinate>	entities;
	private final TileCoordinate					pos;
	private Tile[][]								tiles;

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
		entities = new HashMap<Entity, TileCoordinate>();
		pos = new TileCoordinate( x, y );
		setTiles( tiles );
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
		entities = new HashMap<Entity, TileCoordinate>();
		pos = position;
		setTiles( tiles );
	}

	public TileCoordinate findFreeTile() {
		for ( int i = 1; i < Chunk.CHUNK_SIZE - 1; i++ ) {
			for ( int j = 1; j < Chunk.CHUNK_SIZE - 1; j++ ) {
				if ( getTile( i, j ).getEntityOnTile() == null ) {
					return new TileCoordinate( i, j );
				}
			}
		}
		return null;
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

		int x = pos.getX() * CHUNK_SIZE;
		int y = pos.getY() * CHUNK_SIZE;
		for ( int i = 0; i < tiles.length; i++ ) {
			for ( int j = 0; j < tiles[i].length; j++ ) {
				if ( tiles[i][j].getEntityOnTile() != null ) {
					entities.put( tiles[i][j].getEntityOnTile(), new TileCoordinate( x + i, y + j ) );

				}
			}
		}
	}

	/**
	 * 
	 * @param x
	 *            location of the Tile within the world
	 * @param y
	 *            location of the Tile within the world
	 * @return the Tile t the given location
	 * @throws NullPointerException
	 *             if tiles are still null
	 */
	public Tile getTile( final int x, final int y ) {
		TileCoordinate tile = toArrayIndex( new TileCoordinate( x, y ) );
		return tiles[tile.getX()][tile.getY()];
	}

	/**
	 * returns the coordinates of the given entity within the world
	 * 
	 * @param e
	 *            the entity which position is required
	 * @return the coordinates of the given entity within the world null if this
	 *         entity is not found within this chunk
	 */
	public TileCoordinate getEntityCoordinate( final Entity e ) {
		return entities.get( e );
	}

	@SuppressWarnings("unchecked")
	public synchronized <T extends Entity> T findEntity( final UUID id, final Class<T> clazz ) {
		for ( Entity e : entities.keySet() ) {
			if ( e.getId().equals( id ) && clazz.isInstance( e ) ) {
				return (T) e;
			}
		}
		return null;
	}

	/**
	 * sets the Coordinates of the given entity to the given position if entity
	 * is not already found within this chunk it will be inserted at the given
	 * coordinates
	 * 
	 * @param e
	 *            entity which should be moved
	 * @param x
	 * @param y
	 */
	public synchronized void setEntityCoordinate( final Entity e, final int x, final int y ) {
		TileCoordinate coord = entities.get( e );
		if ( coord != null ) {
			TileCoordinate tile = toArrayIndex( coord );
			tiles[tile.getX()][tile.getY()] = new Tile( tiles[tile.getX()][tile.getY()].getTileType(),
					tiles[tile.getX()][tile.getY()].getHeight() );
		}
		TileCoordinate tile = toArrayIndex( new TileCoordinate( x, y ) );
		tiles[tile.getX()][tile.getY()] = new Tile( tiles[tile.getX()][tile.getY()].getTileType(), e );
		entities.put( e, new TileCoordinate( x, y ) );
	}

	/**
	 * removes the given entity from this chunk if entity is not found within
	 * this chunk no action will be taken
	 * 
	 * @param e
	 *            entity which should be moved
	 */
	public synchronized void removeEntity( final Entity e ) {
		TileCoordinate coord = entities.get( e );
		if ( coord != null ) {
			TileCoordinate tile = toArrayIndex( coord );
			tiles[tile.getX()][tile.getY()] = new Tile( tiles[tile.getX()][tile.getY()].getTileType(),
					tiles[tile.getX()][tile.getY()].getHeight() );
			entities.remove( e );
		}
	}

	/**
	 * will return the position of the {@link Chunk} containing the {@link Tile}
	 * at the given {@link TileCoordinate}
	 * 
	 * @param coordinate
	 * @return the position of the Chunk containing the Tile at the given
	 *         coordinats
	 */
	public static TileCoordinate containingCunk( final TileCoordinate coordinate ) {
		int x, y;
		x = coordinate.getX() / CHUNK_SIZE;
		y = coordinate.getY() / CHUNK_SIZE;
		if ( coordinate.getX() < 0 ) {
			x = x - 1;
		}
		if ( coordinate.getY() < 0 ) {
			y = y - 1;
		}
		return new TileCoordinate( x, y );
	}

	/**
	 * calculates the position of the Tile within this chunk from given
	 * Coordinates within the world
	 * 
	 * @param worldCoordinate
	 * @return
	 */
	private TileCoordinate toArrayIndex( final TileCoordinate worldCoordinate ) {
		int tx = worldCoordinate.getX() % CHUNK_SIZE;
		int ty = worldCoordinate.getY() % CHUNK_SIZE;
		tx = (tx < 0) ? tx + CHUNK_SIZE : tx;
		ty = (ty < 0) ? ty + CHUNK_SIZE : ty;
		return new TileCoordinate( tx, ty );
	}
}