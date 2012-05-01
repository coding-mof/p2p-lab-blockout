package org.blockout.world;


/**
 * 
 * @author Konstantin Ramig
 */
public interface IChunkLoader {
	
	/**
	 * loading Chunk with the given coordinates
	 * @param x coordinate of the Chunk
	 * @param y coordinate of the Chunk
	 * @return the Chunk with the given coordinates
	 */
	public Chunk loadChunk(int x, int y);
}
