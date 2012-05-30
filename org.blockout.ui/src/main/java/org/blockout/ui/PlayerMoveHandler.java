package org.blockout.ui;

import java.util.ArrayList;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.common.TileCoordinate;
import org.blockout.engine.sfx.AudioType;
import org.blockout.engine.sfx.IAudioManager;
import org.blockout.logic.handler.IEventHandler;
import org.blockout.world.IWorld;
import org.blockout.world.LocalGameState;
import org.blockout.world.event.IEvent;
import org.blockout.world.event.PlayerMoveEvent;
import org.blockout.world.state.IStateMachine;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;

/**
 * This class is responsible for handling {@link PlayerMoveEvent}s. It moves the
 * player object using floating values between the tiles. Furthermore it allows
 * the {@link InputHandler} to set a desired {@link Path} which then gets
 * translated into several {@link PlayerMoveEvent}s and processed sequentially.
 * 
 * @author Marc-Christian Schulze
 * 
 */
@Named
public class PlayerMoveHandler implements IEventHandler {

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
	public PlayerMoveHandler(final Camera camera, final LocalGameState gameState, final IWorld world,
			final IAudioManager audioManager) {
		this.camera = camera;
		this.gameState = gameState;
		this.world = world;
		this.audioManager = audioManager;

		walkSounds = new ArrayList<AudioType>();
		walkSounds.add( AudioType.sfx_stonestep1 );
		walkSounds.add( AudioType.sfx_stonestep2 );
		walkSounds.add( AudioType.sfx_stonestep3 );
		rand = new Random( System.currentTimeMillis() );
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
			int oldX = step0 == null ? 0 : step0.getX();
			int oldY = step0 == null ? 0 : step0.getY();
			nextStep++;
			raiseEvent( stateMachine, getNextStep(), oldX, oldY );
		}
	}

	private void raiseEvent( final IStateMachine stateMachine, final Path.Step step, final int oldX, final int oldY ) {
		System.out.println( "raiseEvent: " + step );
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

	@Override
	public void eventStarted( final IStateMachine stateMachine, final IEvent<?> event ) {
		if ( !(event instanceof PlayerMoveEvent) ) {
			return;
		}

		audioManager.getSound( walkSounds.get( rand.nextInt( walkSounds.size() ) ) ).play();

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

		world.setPlayerPosition( pme.getPlayer(), new TileCoordinate( pme.getNewX(), pme.getNewY() ) );

		synchronized ( pathLock ) {
			nextStep++;
			raiseEvent( stateMachine, getNextStep(), pme.getNewX(), pme.getNewY() );
		}
	}
}
