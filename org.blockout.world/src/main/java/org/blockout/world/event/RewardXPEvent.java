package org.blockout.world.event;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Player;

public class RewardXPEvent extends AbstractEvent<RewardXPEvent> {

	private static final long	serialVersionUID	= -7531514056672146621L;
	protected int				experience;
	protected Player			player;

	public RewardXPEvent(final Player p, final int xp) {
		player = p;
		experience = xp;
	}

	public int getExperience() {
		return experience;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public long getDuration() {
		return 0;
	}

	@Override
	public TileCoordinate getResponsibleTile() {
		return new TileCoordinate( 0, 0 );
	}
}
