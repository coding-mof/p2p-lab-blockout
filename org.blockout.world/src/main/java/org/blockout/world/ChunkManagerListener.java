package org.blockout.world;

import org.blockout.common.TileCoordinate;
import org.blockout.network.message.IMessage;

/**
 * Listener that reacts to Messages and Events the {@link IChunkManager} is sending
 * 
 * @author key3
 */
public interface ChunkManagerListener {	
	
	/**
	 * gets invoked when a locally managed chunk is changed 
	 * 
	 * @param msg	the {@link IMessage} that contains all necessary informations
	 * 					for updating the {@link Chunk}
	 */
	public void chunkUpdated(IMessage msg);

}
