package org.blockout.ai;

import org.blockout.common.TileCoordinate;

import com.google.common.base.Preconditions;

public class WalkToPositionTarget implements ITarget {

	protected final TileCoordinate	coord;
	protected AIContext				context;
	protected boolean				initiated;

	public WalkToPositionTarget(final TileCoordinate coord, final AIContext context) {

		Preconditions.checkNotNull( coord );
		Preconditions.checkNotNull( context );

		this.coord = coord;
		this.context = context;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void approach() {
		if ( !initiated ) {
			AIUtils.gotoTile( context, coord );
			initiated = true;
		}
	}

	@Override
	public boolean achieved() {
		TileCoordinate playerPos = context.getWorld().findTile( context.getGameState().getPlayer() );
		return playerPos.equals( coord );
	}

	@Override
	public String toString() {
		return getClass().getName() + "[dest=" + coord + "]";
	}
}
