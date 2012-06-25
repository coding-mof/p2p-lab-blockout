package org.blockout.ai;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Actor;
import org.blockout.world.event.AttackEvent;
import org.blockout.world.state.IStateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * AI task that slays a monster. This task internally acts as a listener to the
 * {@link IStateMachine} and blocks until the monster has been slayed.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class SlayEnemyTarget implements ITarget {

	private static final Logger	logger;
	static {
		logger = LoggerFactory.getLogger( SlayEnemyTarget.class );
	}

	protected AIContext			context;
	protected Actor				enemy;
	protected boolean			achieved;

	public SlayEnemyTarget(final Actor enemy, final AIContext context) {

		Preconditions.checkNotNull( enemy );
		Preconditions.checkNotNull( context );

		this.enemy = enemy;
		this.context = context;
	}

	@Override
	public int hashCode() {
		return ((enemy == null) ? 0 : enemy.hashCode());
	}

	@Override
	public boolean equals( final Object obj ) {
		if ( this == obj ) {
			return true;
		}
		if ( obj == null ) {
			return false;
		}
		if ( getClass() != obj.getClass() ) {
			return false;
		}
		SlayEnemyTarget other = (SlayEnemyTarget) obj;
		if ( enemy == null ) {
			if ( other.enemy != null ) {
				return false;
			}
		} else if ( !enemy.equals( other.enemy ) ) {
			return false;
		}
		return true;
	}

	@Override
	public void approach() {

		TileCoordinate playerPos = context.getWorld().findTile( context.getGameState().getPlayer() );
		TileCoordinate tile = context.getWorld().findTile( enemy );
		if ( tile == null ) {
			logger.debug( "Enemy died. Aborting target..." );
			achieved = true;
			return;
		}
		if ( playerPos.isNeighbour( tile ) ) {
			logger.debug( "Attacking enemy " + enemy + " at " + tile );
			AttackEvent event = new AttackEvent( context.getGameState().getPlayer(), enemy, tile );
			context.getStateMachine().pushEvent( event );
		} else {
			// Enemy moved away - abort target
			logger.debug( "Enemy moved away. Aborting target..." );
			achieved = true;
		}
	}

	@Override
	public int getPriority() {
		return 4;
	}

	@Override
	public boolean achieved() {
		return achieved;
	}

	@Override
	public String toString() {
		return getClass().getName() + "[victim=" + enemy + "]";
	}
}
