package org.blockout.world;

import org.blockout.common.TileCoordinate;


/**
 * 
 * @author Konstantin Ramig
 */
public interface IChunkLoader {
	
	/**
	 * 
	 * @param coordinate coordinates the returned chunk should have
	 * @return the loaded Chunk
	 */
	public Chunk loadChunk(TileCoordinate  coordinate);
}
