package org.blockout.world;

import java.util.Hashtable;

import javax.inject.Named;

import org.blockout.common.TileCoordinate;
import org.blockout.world.event.IEvent;
import org.blockout.world.state.IStateMachine;

/**
 * 
 * @author Konstantin Ramig
 */
@Named
public class DefaultChunkManager implements IChunkManager {

	private IStateMachine						stateMachine;

	private Hashtable<TileCoordinate, Chunk>	chunks;
	
	private ChunkGenerator						chunkGenerator;

	@Override
	public void init( final IStateMachine stateMachine ) {
		this.stateMachine = stateMachine;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void eventCommitted( final IEvent<?> event ) {
		// TODO commitedEvent

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void eventPushed( final IEvent<?> event ) {
		stateMachine.commitEvent( event );
		// TODO pushedEvent
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void eventRolledBack( final IEvent<?> event ) {
		// TODO roledBack
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Chunk requestChunk( final TileCoordinate position ) {
		Chunk chunk = chunkGenerator.generateBasicChunk( position, Chunk.CHUNK_SIZE );
		chunks.put( chunk.getPosition(), chunk );
		return chunk;
		//TODO networkrequest
	}
}
