package org.blockout.ui;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.common.IEvent;
import org.blockout.logic.handler.IEventHandler;
import org.blockout.world.LocalGameState;
import org.blockout.world.event.PlayerMoveEvent;
import org.blockout.world.state.IStateMachine;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;

@Named
public class PlayerMoveHandler implements IEventHandler {

	protected final Camera			camera;
	protected final LocalGameState	gameState;

	private final Object			pathLock	= new Object();
	private Path					path;
	private int						nextStep;
	private IEvent<?>				lastEvent;

	private final Object			posLock		= new Object();
	private float					desiredX;
	private float					desiredY;
	private long					startTime;
	private long					duration;
	private int						startX;
	private int						startY;

	@Inject
	public PlayerMoveHandler(final Camera camera, final LocalGameState gameState) {
		this.camera = camera;
		this.gameState = gameState;
	}

	public void update( final GameContainer container, final int deltaMillis ) {
		long currentTimeMillis = System.currentTimeMillis();

		synchronized ( posLock ) {

			float deltaX = desiredX - startX;
			float deltaY = desiredY - startY;

			float timeDelta = (currentTimeMillis - startTime) / ((float) duration);
			if ( timeDelta > 1 ) {
				timeDelta = 1;
			}

			float currentX = startX + deltaX * timeDelta;
			float currentY = startY + deltaY * timeDelta;

			camera.lock();
			camera.setViewCenter( currentX + 0.5f, currentY + 0.5f );
			camera.unlock();
		}
	}

	public void setPath( final IStateMachine stateMachine, final Path path ) {
		synchronized ( pathLock ) {
			this.path = path;
			nextStep = 0;
			Step step0 = getNextStep();
			nextStep++;
			raiseEvent( stateMachine, getNextStep(), step0.getX(), step0.getY() );
		}
	}

	private void raiseEvent( final IStateMachine stateMachine, final Path.Step step, final int oldX, final int oldY ) {
		if ( step != null ) {
			synchronized ( posLock ) {
				lastEvent = new PlayerMoveEvent( oldX, oldY, step.getX(), step.getY() );
			}
			stateMachine.pushEvent( lastEvent );
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

	@Override
	public void eventStarted( final IStateMachine stateMachine, final IEvent<?> event ) {
		if ( !(event instanceof PlayerMoveEvent) ) {
			return;
		}
		PlayerMoveEvent pme = (PlayerMoveEvent) event;
		synchronized ( posLock ) {
			desiredX = pme.getNewX();
			desiredY = pme.getNewY();
			startX = pme.getOldX();
			startY = pme.getOldY();
			startTime = System.currentTimeMillis();
			duration = event.getDuration();
		}
	}

	@Override
	public void eventFinished( final IStateMachine stateMachine, final IEvent<?> event ) {
		if ( !event.equals( lastEvent ) ) {
			return;
		}
		PlayerMoveEvent pme = (PlayerMoveEvent) event;

		synchronized ( pathLock ) {
			nextStep++;
			raiseEvent( stateMachine, getNextStep(), pme.getNewX(), pme.getNewY() );
		}
	}
}
