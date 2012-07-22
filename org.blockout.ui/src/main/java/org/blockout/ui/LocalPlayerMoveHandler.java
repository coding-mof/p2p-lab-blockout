package org.blockout.ui;

import java.util.ArrayList;
import java.util.Random;

import javax.inject.Inject;

import org.blockout.common.TileCoordinate;
import org.blockout.engine.Camera;
import org.blockout.engine.sfx.AudioType;
import org.blockout.engine.sfx.IAudioManager;
import org.blockout.logic.handler.IEventHandler;
import org.blockout.logic.handler.PlayerMoveHandler;
import org.blockout.world.IWorld;
import org.blockout.world.LocalGameState;
import org.blockout.world.event.IEvent;
import org.blockout.world.event.PlayerMoveEvent;
import org.blockout.world.state.IStateMachine;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;

/**
 * This class is responsible for handling {@link PlayerMoveEvent}s. It moves the
 * player object using floating values between the tiles. Furthermore it allows
 * the {@link InputHandler} to set a desired {@link Path} which then gets
 * translated into several {@link PlayerMoveEvent}s and processed sequentially.
 * This class is only responsible for the local player.
 * 
 * @author Marc-Christian Schulze
 * @see PlayerMoveHandler
 */
public class LocalPlayerMoveHandler implements IEventHandler {

	protected final Camera				camera;
	protected final LocalGameState		gameState;
	protected IWorld					world;
	protected IAudioManager				audioManager;

	private final Object				pathLock	= new Object();
	private Path						path;
	private int							nextStep;
	private IEvent<?>					lastEvent;

	private final Object				posLock		= new Object();
	private float						desiredX;
	private float						desiredY;
	private long						startTime;
	private long						duration;
	private int							startX;
	private int							startY;

	private final ArrayList<AudioType>	walkSounds;
	private final Random				rand;

	@Inject
	public LocalPlayerMoveHandler(final Camera camera, final LocalGameState gameState, final IWorld world) {
		this.camera = camera;
		this.gameState = gameState;
		this.world = world;

		walkSounds = new ArrayList<AudioType>();
		walkSounds.add( AudioType.sfx_stonestep1 );
		walkSounds.add( AudioType.sfx_stonestep2 );
		walkSounds.add( AudioType.sfx_stonestep3 );
		rand = new Random( System.currentTimeMillis() );
	}

	public void setAudioManager( final IAudioManager audioManager ) {
		this.audioManager = audioManager;
	}

	public void update( final int deltaMillis ) {
		long currentTimeMillis = System.currentTimeMillis();

		synchronized ( posLock ) {

			float deltaX = desiredX - startX;
			float deltaY = desiredY - startY;
			if ( deltaX != 0 || deltaY != 0 ) {

				float timeDelta = (currentTimeMillis - startTime) / ((float) duration);
				if ( timeDelta > 1 ) {
					timeDelta = 1;
				}

				float currentX = startX + deltaX * timeDelta;
				float currentY = startY + deltaY * timeDelta;

				camera.setViewCenter( currentX + 0.5f, currentY + 0.5f );
			}
		}
	}

	public void setPath( final IStateMachine stateMachine, final Path path ) {
		synchronized ( pathLock ) {
			this.path = path;
			nextStep = 0;
			Step step0 = getNextStep();
			int oldX = step0 == null ? 0 : step0.getX();
			int oldY = step0 == null ? 0 : step0.getY();
			nextStep++;
			raiseEvent( stateMachine, getNextStep(), oldX, oldY );
		}
	}

	private void raiseEvent( final IStateMachine stateMachine, final Path.Step step, final int oldX, final int oldY ) {
		if ( step != null ) {
			synchronized ( posLock ) {
				lastEvent = new PlayerMoveEvent( gameState.getPlayer(), oldX, oldY, step.getX(), step.getY() );
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

	public TileCoordinate getDestination() {
		synchronized ( pathLock ) {
			if ( path == null || path.getLength() == 0 ) {
				return null;
			}
			Step step = path.getStep( path.getLength() - 1 );
			return new TileCoordinate( step.getX(), step.getY() );
		}
	}

	@Override
	public void eventStarted( final IStateMachine stateMachine, final IEvent<?> event ) {
		if ( !(event instanceof PlayerMoveEvent) ) {
			return;
		}
		PlayerMoveEvent pme = (PlayerMoveEvent) event;
		if ( !pme.getPlayer().equals( gameState.getPlayer() ) ) {
			// we handle only the movements of our local player
			return;
		}

		if ( audioManager != null ) {
			audioManager.getSound( walkSounds.get( rand.nextInt( walkSounds.size() ) ) ).play();
		}

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
		if ( !pme.getPlayer().equals( gameState.getPlayer() ) ) {
			// we handle only the movements of our local player
			return;
		}

		// world.setPlayerPosition( pme.getPlayer(), new TileCoordinate(
		// pme.getNewX(), pme.getNewY() ) );

		synchronized ( pathLock ) {
			nextStep++;
			raiseEvent( stateMachine, getNextStep(), pme.getNewX(), pme.getNewY() );
		}
	}
}
