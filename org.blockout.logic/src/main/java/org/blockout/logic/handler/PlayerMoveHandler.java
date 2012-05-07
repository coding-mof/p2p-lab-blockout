package org.blockout.logic.handler;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.common.IEvent;
import org.blockout.world.LocalGameState;
import org.blockout.world.event.PlayerMoveEvent;
import org.blockout.world.state.IStateMachine;
import org.blockout.world.state.IStateMachineListener;

@Named
public class PlayerMoveHandler implements IStateMachineListener {

	protected LocalGameState	gameState;

	@Inject
	public PlayerMoveHandler(final LocalGameState gameState) {
		this.gameState = gameState;
	}

	@Override
	public void eventCommitted( final IEvent<?> event ) {
	}

	@Override
	public void eventPushed( final IEvent<?> event ) {
		if ( event instanceof PlayerMoveEvent ) {
			PlayerMoveEvent pme = (PlayerMoveEvent) event;
			gameState.setDesiredPlayerX( pme.getNewX() );
			gameState.setDesiredPlayerY( pme.getNewY() );
		}
	}

	@Override
	public void eventRolledBack( final IEvent<?> event ) {
		if ( event instanceof PlayerMoveEvent ) {
			PlayerMoveEvent pme = (PlayerMoveEvent) event;
			gameState.setDesiredPlayerX( pme.getNewX() );
			gameState.setDesiredPlayerY( pme.getNewY() );
		}
	}

	@Override
	public void init( final IStateMachine stateMachine ) {
	}
}
