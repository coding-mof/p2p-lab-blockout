package org.blockout.world.event;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Crate;

import com.google.common.base.Preconditions;

public class CrateCreatedEvent extends AbstractEvent<CrateOpenedEvent> {

	private static final long	serialVersionUID	= 8321214157754903797L;
	protected Crate				crate;
	protected TileCoordinate	crateCoord;

	public CrateCreatedEvent(final Crate crate, final TileCoordinate crateCoord) {

		Preconditions.checkNotNull( crate );
		Preconditions.checkNotNull( crateCoord );

		this.crate = crate;
		this.crateCoord = crateCoord;
	}

	/**
	 * Returns the crate that gets opened.
	 * 
	 * @return The crate that gets opened.
	 */
	public Crate getCrate() {
		return crate;
	}

	@Override
	public long getDuration() {
		return 100;
	}

	@Override
	public TileCoordinate getResponsibleTile() {
		return crateCoord;
	}

	@Override
	public String toString() {
		return "CrateCreatedEvent[cate=" + crate + ", coord=" + crateCoord + ", id=" + getId() + "]";
	}
}
