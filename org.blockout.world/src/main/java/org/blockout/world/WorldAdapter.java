package org.blockout.world;

import org.blockout.common.TileCoordinate;

public interface WorldAdapter {
	
	/**
	 * Will add the given chunk to the list of the chunks
	 * this world is responsible for
	 * if there is already a chunk with the coordinates of the given chunk
	 * no actions will be taken
	 * 
	 * @param chunk
	 */
	public void manageChunk(Chunk chunk);
	
	/**
	 * Will remove the chunk  at the given coordinates from the list
	 * of the chunks this world is responsible for
	 * 
	 * @param coord the coordinates of the chunk the world should 
	 * 					no longer be responsible for
	 * @return	the Chunk this World is no longer responsible for
	 */
	public Chunk unmanageChunk(TileCoordinate coord);
	
	/**
	 * Will return the Chunk with the given coordinates from
	 * the list of Chunks this World is responsible for
	 * if there is no chunk with the given coordinates it will 
	 * be generated and added to the list of Chunks the World is responsible for
	 * 
	 * @param coord coordinates of the desired Chunk
	 * @return the Chunk at the given coordinates
	 */
	public Chunk getChunk(TileCoordinate coord);
}
