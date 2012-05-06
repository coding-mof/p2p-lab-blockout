package org.blockout.world.event;

import java.util.Calendar;
import java.util.UUID;

import org.blockout.common.IEvent;
import org.blockout.world.entity.Actor;

public class AttackEvent implements IEvent<AttackEvent> {
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
	public UUID getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Calendar getLocalTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInverseOf( final IEvent<AttackEvent> event ) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AttackEvent getInverse() {
		// TODO Auto-generated method stub
		return null;
	}
}
