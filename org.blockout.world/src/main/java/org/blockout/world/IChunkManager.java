package org.blockout.world;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Player;

/**
 * Interface for the core component for managing the world. A chunk manager
 * can manage multiple {@link Chunk}s which are the partitions of the world
 * 
 * @author Konstantin Ramig
 */
public interface IChunkManager {

	/**
	 * Will send a request using the Network for the Chunk with the given position
	 * will call the addRequestedChunk method of the {@link IWorld}}
	 * 
	 * the responsible manager for this chunk will also start sending
	 * Committed events to this manager
	 * 
	 * @param position position of the requested Chunk
	 */
	public void requestChunk(TileCoordinate position);
	
	/**
	 * stops requesting updates of the chunk at the given coordinates
	 * 
	 * @param position position of the chunk that should no longer be updated
	 */
	public void stopUpdating(TileCoordinate position);
	
	public void enterGame(Player player);
	
	public void addListener(ChunkManagerListener listener);
	
	public void removeListener(ChunkManagerListener listener);
}
