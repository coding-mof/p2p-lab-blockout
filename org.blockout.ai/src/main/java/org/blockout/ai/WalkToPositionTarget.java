package org.blockout.ai;

import org.blockout.common.TileCoordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * The {@link AIPlayer} currently wants to walk to a certain tile.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class WalkToPositionTarget implements ITarget {

	private static final Logger		logger;
	static {
		logger = LoggerFactory.getLogger( WalkToPositionTarget.class );
	}

	protected final TileCoordinate	coord;
	protected AIContext				context;
	protected boolean				initiated;
	protected boolean				aborted;
	protected int					priority;
	private final String			reason;

	public WalkToPositionTarget(final TileCoordinate coord, final AIContext context, final int priority,
			final String reason) {

		Preconditions.checkNotNull( coord );
		Preconditions.checkNotNull( context );

		this.coord = coord;
		this.context = context;
		this.priority = priority;
		this.reason = reason;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public void approach() {
		if ( initiated ) {
			TileCoordinate currentDestination = AIUtils.getCurrentDestination( context );
			if ( !currentDestination.equals( coord ) ) {
				// re-initiate if the destination differs
				logger.debug( "Re-initiating walking since current destination " + currentDestination
						+ " is not the desired " + coord );
				initiated = false;
			}
		}
		if ( !initiated ) {
			if ( !AIUtils.gotoTile( context, coord ) ) {
				// no way to get to destination
				logger.debug( "Aborted walking since there is no way to get to " + coord );
				aborted = true;
			}
			initiated = true;
		}
	}

	@Override
	public boolean achieved() {
		TileCoordinate playerPos = context.getWorld().findTile( context.getGameState().getPlayer() );
		return aborted || playerPos.equals( coord );
	}

	@Override
	public String toString() {
		return getClass().getName() + "[dest=" + coord + ", reason=" + reason + "]";
	}
}
