package org.blockout.ai;

import org.blockout.common.TileCoordinate;

import com.google.common.base.Preconditions;

public class WalkToPositionTarget implements ITarget {

	protected final TileCoordinate	coord;
	protected AIContext				context;
	protected boolean				initiated;
	protected boolean				aborted;
	protected int					priority;

	public WalkToPositionTarget(final TileCoordinate coord, final AIContext context, final int priority) {

		Preconditions.checkNotNull( coord );
		Preconditions.checkNotNull( context );

		this.coord = coord;
		this.context = context;
		this.priority = priority;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public void approach() {
		if ( !initiated ) {
			if ( !AIUtils.gotoTile( context, coord ) ) {
				// no way to get to destination
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
		return getClass().getName() + "[dest=" + coord + "]";
	}
}
