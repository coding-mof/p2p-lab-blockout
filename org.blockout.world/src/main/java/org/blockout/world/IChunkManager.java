package org.blockout.world;

/**
 * 
 * @author Konstantin Ramig
 */
public interface IChunkManager {
	
	/**
	 * Method adds the given Chunk to the list of Chunks this ChunkManager is
	 * responsible for
	 * 
	 * @param c the IChunk this Manager should start managing 
	 */
	public void manageChunk(Chunk c);
	
	/**
	 * Getting one certain Chunk this ChunkManager is responsible for
	 * 
	 * @param x coordinate of the Chunk
	 * @param y coordinate of the Chunk
	 * @return the Chunk with the given coordinates, 
	 * 			null if ChunkManager doesn't contain he Chunk yet
	 */
	public Chunk getChunk(int x, int y);
	
	/**
	 * Updating a Chunk this ChunkManager is responsible for
	 * 
	 * @param c the Chunk that should be updated
	 * @return true if update was successful
	 * 			false if the ChunkManager doesn't know the Chunk
	 * 			false if any other error occurred
	 */
	public boolean updateChunk(Chunk c);

}
