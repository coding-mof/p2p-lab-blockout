package org.blockout.world;

import javax.inject.Inject;
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
	
	@Inject private WorldAdapter 				worldAdapter;
	
	//@Inject private INetworkInterface			network;

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
	public void requestChunk( final TileCoordinate position ) {
		worldAdapter.getChunk(position);
		//TODO networkrequest
	}

	@Override
	public void stopUpdating(TileCoordinate position) {
		// TODO stop networkrequest
		
	}
}
