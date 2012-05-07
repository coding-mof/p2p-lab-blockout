package org.blockout.world.event;

import java.util.Calendar;
import java.util.UUID;

import org.blockout.common.IEvent;
import org.blockout.world.entity.Player;

public class PlayerDiedEvent implements IEvent<PlayerDiedEvent> {

	protected Player	player;

	public PlayerDiedEvent(final Player player) {
		this.player = player;
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
	public boolean isInverseOf( final IEvent<PlayerDiedEvent> event ) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PlayerDiedEvent getInverse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getDuration() {
		return 100;
	}

}
