package org.blockout.world;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.world.entity.Player;

@Named
public class LocalGameState {

	protected Player	player;

	protected float		currentPlayerX;
	protected float		currentPlayerY;
	protected float		desiredPlayerX;
	protected float		desiredPlayerY;

	@Inject
	public LocalGameState(final IWorld world) {

	}

	public float getCurrentPlayerX() {
		return currentPlayerX;
	}

	public void setCurrentPlayerX( final float currentPlayerX ) {
		this.currentPlayerX = currentPlayerX;
	}

	public float getCurrentPlayerY() {
		return currentPlayerY;
	}

	public void setCurrentPlayerY( final float currentPlayerY ) {
		this.currentPlayerY = currentPlayerY;
	}

	public float getDesiredPlayerX() {
		return desiredPlayerX;
	}

	public void setDesiredPlayerX( final float desiredPlayerX ) {
		this.desiredPlayerX = desiredPlayerX;
	}

	public float getDesiredPlayerY() {
		return desiredPlayerY;
	}

	public void setDesiredPlayerY( final float desiredPlayerY ) {
		this.desiredPlayerY = desiredPlayerY;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer( final Player player ) {
		this.player = player;
	}
}
