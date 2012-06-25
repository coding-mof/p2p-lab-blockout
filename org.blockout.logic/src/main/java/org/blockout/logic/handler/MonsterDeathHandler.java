package org.blockout.logic.handler;

import java.util.Random;

import javax.inject.Inject;

import org.blockout.common.TileCoordinate;
import org.blockout.world.IWorld;
import org.blockout.world.entity.Crate;
import org.blockout.world.entity.Player;
import org.blockout.world.event.IEvent;
import org.blockout.world.event.MonsterSlayedEvent;
import org.blockout.world.event.RewardXPEvent;
import org.blockout.world.state.IStateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * This class handles {@link MonsterSlayedEvent}s and spawns items that get
 * dropped.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class MonsterDeathHandler implements IEventHandler {

	private static final Logger	logger;
	static {
		logger = LoggerFactory.getLogger( MonsterDeathHandler.class );
	}
	protected IWorld			world;
	private final Random		rand;

	@Inject
	public MonsterDeathHandler(final IWorld world) {

		Preconditions.checkNotNull( world );

		this.world = world;
		rand = new Random( System.currentTimeMillis() );
	}

	@Override
	public void eventStarted( final IStateMachine stateMachine, final IEvent<?> event ) {
		// TODO: play monster death sound
	}

	@Override
	public void eventFinished( final IStateMachine stateMachine, final IEvent<?> event ) {
		if ( !(event instanceof MonsterSlayedEvent) ) {
			return;
		}

		MonsterSlayedEvent mse = (MonsterSlayedEvent) event;
		if ( mse.getActor() instanceof Player ) {
			RewardXPEvent xpEvent = new RewardXPEvent( (Player) mse.getActor(), 75 );
			stateMachine.pushEvent( xpEvent );
		}

		TileCoordinate coordinate = world.findTile( mse.getMonster() );
		if ( coordinate != null ) {
			world.removeEntity( mse.getMonster() );

			int porbability = 60;
			logger.info( porbability + "% probability to spawn a crate..." );
			if ( rand.nextInt( 100 ) < porbability ) {
				logger.info( "Spawning crate on old monster position " + coordinate );
				world.setEnityPosition( new Crate(), coordinate );
			}
		}
	}
}
