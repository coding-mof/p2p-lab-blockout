package org.blockout.ai;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Crate;
import org.blockout.world.event.CrateOpenedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * The {@link AIPlayer} currently wants to open a crate.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class OpenCrateTarget implements ITarget {
	private static final Logger	logger;
	static {
		logger = LoggerFactory.getLogger( OpenCrateTarget.class );
	}

	protected Crate				crate;
	protected AIContext			context;
	protected boolean			achieved;

	public OpenCrateTarget(final Crate crate, final AIContext context) {

		Preconditions.checkNotNull( crate );
		Preconditions.checkNotNull( context );

		this.crate = crate;
		this.context = context;
	}

	@Override
	public int getPriority() {
		return 3;
	}

	@Override
	public void approach() {

		// activate crate
		crate = context.getWorld().findEntity( crate.getId(), Crate.class );
		if ( crate == null ) {
			// Who stole the crate?
			logger.warn( "Crate " + crate + " disappeared." );
			achieved = true;
			return;
		}

		TileCoordinate playerPos = context.getWorld().findTile( context.getGameState().getPlayer() );
		TileCoordinate tile = context.getWorld().findTile( crate );
		if ( tile == null ) {
			// Who stole the crate?
			logger.warn( "Crate " + crate + " disappeared." );
			achieved = true;
		}
		if ( playerPos.isNeighbour( tile ) ) {
			if ( crate.getItem() != null ) {
				logger.debug( "Taking " + crate.getItem() + " out of crate " + crate );

				CrateOpenedEvent event = new CrateOpenedEvent( context.getGameState().getPlayer(), crate, tile );
				context.getStateMachine().pushEvent( event );
			} else {
				logger.debug( "Ignoring empty crate." );
				achieved = true;
			}
		} else {
			// Why has the crate been moved?
			logger.warn( "Crate " + crate + " has been moved to " + tile );
			achieved = true;
		}
	}

	@Override
	public boolean achieved() {
		return achieved;
	}
}
