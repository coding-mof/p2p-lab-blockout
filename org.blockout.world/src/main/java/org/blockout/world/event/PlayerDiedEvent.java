package org.blockout.world.event;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Player;

/**
 * Events of this class are generated when a player died.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class PlayerDiedEvent extends AbstractEvent<PlayerDiedEvent> {

	private static final long	serialVersionUID	= -5982280022093118648L;
	protected Player			player;

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
