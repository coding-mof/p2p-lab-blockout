package org.blockout.world.event;

import java.util.Calendar;
import java.util.UUID;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Crate;
import org.blockout.world.entity.Player;

public class CrateOpenedEvent implements IEvent<CrateOpenedEvent> {

	protected Player	player;
	protected Crate		crate;

	public CrateOpenedEvent(final Player player, final Crate crate) {
		this.player = player;
		this.crate = crate;
	}

	public Player getPlayer() {
		return player;
	}

	public Crate getCrate() {
		return crate;
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
		return 200;
	}

	@Override
	public TileCoordinate getResponsibleTile() {
		// TODO Auto-generated method stub
		return null;
	}

}
