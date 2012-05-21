package org.blockout.logic.handler;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.common.TileCoordinate;
import org.blockout.world.IWorld;
import org.blockout.world.entity.Crate;
import org.blockout.world.event.IEvent;
import org.blockout.world.event.MonsterSlayedEvent;
import org.blockout.world.state.IStateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles {@link MonsterSlayedEvent}s and spawns items that get
 * dropped.
 * 
 * @author Marc-Christian Schulze
 * 
 */
@Named
public class MonsterDeathHandler implements IEventHandler {

	private static final Logger	logger;
	static {
		logger = LoggerFactory.getLogger( MonsterDeathHandler.class );
	}
	protected IWorld			world;

	@Inject
	public MonsterDeathHandler(final IWorld world) {
		this.world = world;
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
		world.removeEntity( mse.getMonster() );
		TileCoordinate coordinate = world.findTile( mse.getMonster() );
		if ( coordinate != null ) {
			logger.info( "Spawning crate on old monster position " + coordinate );
			world.setEnityPosition( new Crate(), coordinate );
		}
	}
}
