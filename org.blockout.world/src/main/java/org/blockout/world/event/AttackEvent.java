package org.blockout.world.event;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Actor;

public class AttackEvent extends AbstractEvent<AttackEvent> {
	protected Actor	aggressor;
	protected Actor	victim;

	public AttackEvent(final Actor aggressor, final Actor victim) {
		this.aggressor = aggressor;
		this.victim = victim;
	}

	public Actor getAggressor() {
		return aggressor;
	}

	public Actor getVictim() {
		return victim;
	}

	@Override
	public long getDuration() {
		return 200;
	}

	@Override
	public TileCoordinate getResponsibleTile() {
		// TODO Auto-generated method stub
		return null;
	}
}
