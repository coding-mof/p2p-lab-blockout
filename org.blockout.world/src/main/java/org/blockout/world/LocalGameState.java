package org.blockout.world;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.world.entity.Player;

@Named
public class LocalGameState {

	protected Player	player;

	@Inject
	public LocalGameState(final IWorld world) {

	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer( final Player player ) {
		this.player = player;
	}
}
