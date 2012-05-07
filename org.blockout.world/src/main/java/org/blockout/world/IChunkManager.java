package org.blockout.world;

import org.blockout.common.Tupel;
import org.blockout.world.state.IStateMachineListener;

/**
 * Interface for the core component for managing the world. A chunk manager
 * can manage multiple {@link Chunk}s which are the partitions of the world
 * 
 * @author Konstantin Ramig
 */
public interface IChunkManager extends IStateMachineListener {

	/**
	 * Returns the Chunk at the requested position
	 * 
	 * @param position position if the requested Chunk
	 * @return Chunk at the requested position
	 */
	public Chunk getChunk(Tupel position);
}
