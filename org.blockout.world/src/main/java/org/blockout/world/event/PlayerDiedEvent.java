package org.blockout.world.event;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Player;

public class PlayerDiedEvent extends AbstractEvent<PlayerDiedEvent> {

	protected Player	player;

	public PlayerDiedEvent(final Player player) {
		this.player = player;
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
