package org.blockout.ui;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.common.IEvent;
import org.blockout.world.LocalGameState;
import org.blockout.world.event.PlayerMoveEvent;
import org.blockout.world.state.IStateMachine;
import org.blockout.world.state.IStateMachineListener;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.util.pathfinding.Path;

@Named
public class PlayerController {

	// How long it takes to move the player to a new position. measured in
	// milliseconds
	private static final float		PLAYER_MOVE_TIME	= 500.0f;

	private float					playerPosX			= 0;
	private float					playerPosY			= 0;

	protected final IStateMachine	stateMachine;
	protected final Camera			camera;
	protected final LocalGameState	gameState;
	private final Listener			listener;

	private final Object			pathLock			= new Object();
	private Path					path;
	private int						nextStep;

	@Inject
	public PlayerController(final Camera camera, final LocalGameState gameState, final IStateMachine stateMachine) {
		this.camera = camera;
		this.gameState = gameState;
		this.stateMachine = stateMachine;
		listener = new Listener();
		stateMachine.addIStateMachineListener( listener );
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		stateMachine.removeIStateMachineListener( listener );
	}

	public void update( final GameContainer container, final int deltaMillis ) {
		float currentX = gameState.getCurrentPlayerX();
		float currentY = gameState.getCurrentPlayerY();
		float desiredX = gameState.getDesiredPlayerX();
		float desiredY = gameState.getDesiredPlayerY();

		// otherwise we would raise an event flood
		if ( currentX != desiredX || currentY != desiredY ) {

			float deltaX = desiredX - currentX;
			playerPosX += deltaX * (deltaMillis / PLAYER_MOVE_TIME);
			if ( (deltaX > 0 && playerPosX >= desiredX) || (deltaX < 0 && playerPosX <= desiredX) ) {
				playerPosX = desiredX;
				gameState.setCurrentPlayerX( gameState.getDesiredPlayerX() );
			}

			float deltaY = desiredY - currentY;
			playerPosY += deltaY * (deltaMillis / PLAYER_MOVE_TIME);
			if ( (deltaY > 0 && playerPosY >= desiredY) || (deltaY < 0 && playerPosY <= desiredY) ) {
				playerPosY = desiredY;
				gameState.setCurrentPlayerY( gameState.getDesiredPlayerY() );
			}

		}
		camera.setViewCenter( playerPosX + 0.5f, playerPosY + 0.5f );
	}

	public void setPath( final Path path ) {
		synchronized ( pathLock ) {
			this.path = path;
			nextStep = 1;

			raiseEvent( getNextStep() );
		}
	}

	private void raiseEvent( final Path.Step step ) {
		if ( step != null ) {
			stateMachine
					.pushEvent( new PlayerMoveEvent( (int) playerPosX, (int) playerPosY, step.getX(), step.getY() ) );
		}
	}

	private Path.Step getNextStep() {
		if ( path == null ) {
			return null;
		}
		if ( path.getLength() <= nextStep ) {
			return null;
		}
		return path.getStep( nextStep );
	}

	private class Listener implements IStateMachineListener {

		@Override
		public void eventCommitted( final IEvent<?> event ) {
			synchronized ( pathLock ) {
				nextStep++;
				raiseEvent( getNextStep() );
			}
		}

		@Override
		public void eventPushed( final IEvent<?> event ) {
		}

		@Override
		public void eventRolledBack( final IEvent<?> event ) {
		}

		@Override
		public void init( final IStateMachine stateMachine ) {
		}
	}

}
