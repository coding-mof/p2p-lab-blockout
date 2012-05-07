package org.blockout.world;

import org.blockout.common.TileCoordinate;

/**
 * 
 * @author Konstantin Ramig
 */
public class ChunkGenerator {

	/**
	 * generates basic Chunk without any Entities or walls on it
	 * 
	 * @param pos position of the Chunk in the World
	 * @param size size of the Tilematrix
	 * @return an Chunk with the given coordinates an a Tilematrix sizeXsize
	 */
	public static Chunk generateBasicChunk( TileCoordinate pos, int size ){
		Tile[][] tiles = new Tile[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				tiles[i][j] = new Tile(848);
			}
		}
		return new Chunk(pos, tiles);
	}
}
