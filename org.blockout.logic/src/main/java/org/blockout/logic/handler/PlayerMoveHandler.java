package org.blockout.logic.handler;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.world.IWorld;
import org.blockout.world.LocalGameState;
import org.blockout.world.event.IEvent;
import org.blockout.world.event.PlayerMoveEvent;
import org.blockout.world.state.IStateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This event handler is responsible for moving all players except the own/local
 * one.
 * 
 * @author Marc-Christian Schulze
 * @see LocalPlayerMoveHandler
 */
@Named
public class PlayerMoveHandler implements IEventHandler {

	private static final Logger	logger;
	static {
		logger = LoggerFactory.getLogger( PlayerMoveHandler.class );
	}

	protected LocalGameState	gameState;
	protected IWorld			world;

	@Inject
	public PlayerMoveHandler(final LocalGameState gameState, final IWorld world) {
		this.gameState = gameState;
		this.world = world;
	}

	@Override
	public void eventStarted( final IStateMachine stateMachine, final IEvent<?> event ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void eventFinished( final IStateMachine stateMachine, final IEvent<?> event ) {
		if ( !(event instanceof PlayerMoveEvent) ) {
			return;
		}
		PlayerMoveEvent pme = (PlayerMoveEvent) event;
		if ( pme.getPlayer().equals( gameState.getPlayer() ) ) {
			// we handle only the movements of "strangers"
			return;
		}
		logger.info( "Player " + pme.getPlayer() + " moved from tile " + pme.getOldPos() + " to tile "
				+ pme.getNewPos() );
		world.setPlayerPosition( pme.getPlayer(), pme.getNewPos() );
	}

}
