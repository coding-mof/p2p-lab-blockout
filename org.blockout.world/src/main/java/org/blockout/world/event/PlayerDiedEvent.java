package org.blockout.world.event;

import java.util.Calendar;
import java.util.UUID;

import org.blockout.common.TileCoordinate;
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
	public long getDuration() {
		return 100;
	}

	@Override
	public TileCoordinate getResponsibleTile() {
		// TODO Auto-generated method stub
		return null;
	}

}
