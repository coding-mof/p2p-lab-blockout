package org.blockout.world;

import org.blockout.common.TileCoordinate;

/**
 * 
 * @author Konstantin Ramig
 */
public interface IChunkGenerator {

	/**
	 * Generator will return a random Chunk size Chunk.CHUNK_SIZExChunk.CHUNK_SIZE
	 * with the given coordinates
	 * 
	 * @param coordinate
	 * @return
	 */
	public Chunk generateChunk( TileCoordinate coordinate);
}
