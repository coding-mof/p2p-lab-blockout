package org.blockout.world;

import java.io.File;

import org.blockout.common.TileCoordinate;


/**
 * 
 * @author Konstantin Ramig
 */
public interface IChunkLoader {
	
	/**
	 * Loads a predefined Chunk from an tmx-file
	 * 
	 * @param coordinate coordinates the Chunk should have
	 * @param tmxFile the File from which the Chunk should be loaded
	 * @return the desired Chunk
	 */
	public Chunk loadChunk(TileCoordinate  coordinate, String tmxFile);
	
	/**
	 * will return a list of predefined maps
	 * 
	 * @return
	 */
	public File[] getMaps();
}
