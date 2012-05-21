package org.blockout.world;

import javax.inject.Named;

import org.blockout.common.TileCoordinate;
import org.blockout.engine.SpriteMapping;
import org.blockout.engine.SpriteType;

/**
 * 
 * @author Konstantin Ramig
 */
@Named
public class BasicChunkGenerator implements IChunkGenerator{
	
	private SpriteMapping mapping = new SpriteMapping();

	/**
	 * generates basic Chunk without any Entities or walls on it
	 * 
	 * @param pos position of the Chunk in the World
	 * @param size size of the Tilematrix
	 * @return an Chunk with the given coordinates an a Tilematrix sizeXsize
	 */
	public Chunk generateBasicChunk( TileCoordinate pos, int size ){
		Tile[][] tiles = new Tile[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				tiles[i][j] = new Tile(mapping.getSpriteId(SpriteType.stoneground));
			}
		}
		return new Chunk(pos, tiles);
	}


	@Override
	public Chunk generateChunk(TileCoordinate coordinate) {
		return generateBasicChunk(coordinate, Chunk.CHUNK_SIZE);
	}
	

}
