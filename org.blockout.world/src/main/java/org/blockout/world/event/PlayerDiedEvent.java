package org.blockout.world.event;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Player;

import com.google.common.base.Preconditions;

/**
 * Events of this class are generated when a player died.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class PlayerDiedEvent extends AbstractEvent<PlayerDiedEvent> {

	private static final long	serialVersionUID	= -5982280022093118648L;
	protected Player			player;
	protected TileCoordinate	deathCoord;

	public PlayerDiedEvent(final Player player, final TileCoordinate deathCoord) {

		Preconditions.checkNotNull( player );
		Preconditions.checkNotNull( deathCoord );

		this.player = player;
		this.deathCoord = deathCoord;
	}

	@Override
	public long getDuration() {
		return 100;
	}

	@Override
	public TileCoordinate getResponsibleTile() {
		return deathCoord;
	}

}
