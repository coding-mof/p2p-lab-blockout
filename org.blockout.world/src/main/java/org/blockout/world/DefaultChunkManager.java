package org.blockout.world;

import java.util.Hashtable;

import javax.inject.Named;

import org.blockout.common.IEvent;
import org.blockout.common.Tupel;
import org.blockout.world.state.IStateMachine;
import org.blockout.world.state.IStateMachineListener;

/**
 * 
 * @author Konstantin Ramig
 */
@Named
public class DefaultChunkManager implements IChunkManager {
	
	private IStateMachine stateMachine;
	
	private Hashtable<Tupel, Chunk> chunks;
	

	@Override
	public void init(IStateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void eventCommitted(IEvent<?> event) {
		// TODO commitedEvent
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void eventPushed(IEvent<?> event) {
		stateMachine.commitEvent(event);
		// TODO pushedEvent
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void eventRolledBack(IEvent<?> event) {
		// TODO roledBack		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Chunk getChunk(Tupel position) {
		Chunk chunk = ChunkGenerator.generateBasicChunk(position, Chunk.CHUNK_SIZE);
		chunks.put(chunk.getPosition(), chunk);
		return chunk;
	}

}
