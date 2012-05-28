package org.blockout.world;

import java.io.Serializable;
import java.util.HashMap;
import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Entity;

import com.google.common.base.Preconditions;

/**
 * 
 * @author Konstantin Ramig
 */
public class Chunk implements Serializable{

	private static final long serialVersionUID = 6458398672895072597L;

	public static final int CHUNK_SIZE = 48;

	private HashMap<Entity, TileCoordinate> 	entitys;
	private final TileCoordinate 				pos;
	private Tile[][] 							tiles;

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
	public Chunk(final int x, final int y, final Tile[][] tiles)
			throws IllegalArgumentException {
		entitys = new HashMap<Entity, TileCoordinate>();
		pos = new TileCoordinate(x, y);
		setTiles(tiles);
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
	public Chunk(final TileCoordinate position, final Tile[][] tiles)
			throws IllegalArgumentException {
		entitys = new HashMap<Entity, TileCoordinate>();
		pos = position;
		setTiles(tiles);
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
	public void setTiles(final Tile[][] tiles) {
		Preconditions.checkNotNull(tiles);
		if (tiles.length != CHUNK_SIZE) {
			throw new IllegalArgumentException("Tile matrix is not symetric");
		}
		for (Tile[] tile : tiles) {
			if (tile.length != CHUNK_SIZE) {
				throw new IllegalArgumentException(
						"Tile matrix is not symetric");
			}
		}
		
		this.tiles = tiles;

		int x = pos.getX() * CHUNK_SIZE;
		int y = pos.getY() * CHUNK_SIZE;
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				if (tiles[i][j].getEntityOnTile() != null) {
					entitys.put(tiles[i][j].getEntityOnTile(),
							new TileCoordinate(x + i, y + j));
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
	public Tile getTile(final int x, final int y) {
		int x2 = x % CHUNK_SIZE;
		int y2 = y % CHUNK_SIZE;
		x2 = (x < 0) ? CHUNK_SIZE + x2 : x2;
		y2 = (x < 0) ? CHUNK_SIZE + y2 : y2;
		return tiles[x2][y2];
	}
	
	/**
	 * returns the coordinates of the given entity within the world
	 * 
	 * @param e the entity which position is required
	 * @return	the coordinates of the given entity within the world
	 * 			null if this entity is not found within this chunk
	 */
	public TileCoordinate getEntityCoordinate(final Entity e) {
		return entitys.get(e);		
	}
	
	/**
	 * sets the Coordinates of the given entity to the given position
	 * 	if entity is not already found within this chunk it will be
	 * 	inserted at the given coordinates
	 * 
	 * @param e entity which should be moved
	 * @param x
	 * @param y
	 */
	public void setEntityCoordinate(final Entity e, final int x, final int y) {
		TileCoordinate coord = entitys.get(e);
		if (coord != null) {
			int tx = coord.getX() % CHUNK_SIZE;
			int ty = coord.getY() % CHUNK_SIZE;
			tx = (tx < 0) ? tx - 1 : tx;
			ty = (ty < 0) ? ty - 1 : ty;
			tiles[tx][ty] = new Tile(tiles[tx][ty].getTileType(),
					tiles[tx][ty].getHeight());
		}
		int tx = x % CHUNK_SIZE;
		int ty = y % CHUNK_SIZE;
		tx = (tx < 0) ? tx - 1 : tx;
		ty = (ty < 0) ? ty - 1 : ty;
		tiles[tx][ty] = new Tile(tiles[tx][ty].getTileType(),
				e);
		entitys.put(e, new TileCoordinate(x, y));
	}
	
	
	
	/**
	 * removes the given entity from this chunk
	 * 	if entity is not found within this chunk no action will be taken
	 * 
	 * @param e entity which should be moved
	 */
	public void removeEntity(final Entity e) {
		TileCoordinate coord = entitys.get(e);
		if (coord != null) {
			int tx = coord.getX() % CHUNK_SIZE;
			int ty = coord.getY() % CHUNK_SIZE;
			tx = (tx < 0) ? tx - 1 : tx;
			ty = (ty < 0) ? ty - 1 : ty;
			tiles[tx][ty] = new Tile(tiles[tx][ty].getTileType(),
					tiles[tx][ty].getHeight());
			entitys.remove(e);
		}
	}
	
	/**
	 * will return the position of the {@link Chunk} containing the {@link Tile}
	 * at the given {@link TileCoordinate}
	 * 
	 * @param coordinate 
	 * @return the position of the Chunk containing the Tile at the given coordinats
	 */
	public static TileCoordinate containingCunk(final TileCoordinate coordinate){
		int x,y;
		x= coordinate.getX()/CHUNK_SIZE;
		y= coordinate.getY()/CHUNK_SIZE;
		if (x < 0) {
			x = x - 1;
		}
		if (y < 0) {
			y = y - 1;
		}
		return new TileCoordinate(x, y);
	}
}