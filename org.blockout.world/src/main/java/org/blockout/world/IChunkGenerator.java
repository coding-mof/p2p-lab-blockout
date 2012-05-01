package org.blockout.world;


/**
 * 
 * @author Konstantin Ramig
 */
public interface IChunkGenerator {
	
	/**
	 * 
	 * @param x 
	 * @param y
	 * @param size
	 * @return an Chunk with the given coordinates an a Tilematrix sizeXsize
	 */
	public Chunk generateChunk(int x, int y, int size);
}
