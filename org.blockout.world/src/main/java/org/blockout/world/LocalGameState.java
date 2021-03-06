package org.blockout.world;

import javax.inject.Inject;

import org.blockout.world.entity.Player;

public class LocalGameState {

	protected Player			player;
	protected volatile boolean	gameInitialized;

	public boolean isGameInitialized() {
		return gameInitialized;
	}

	public void setGameInitialized( final boolean gameInitialized ) {
		this.gameInitialized = gameInitialized;
	}

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
