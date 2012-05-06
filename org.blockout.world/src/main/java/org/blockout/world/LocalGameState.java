package org.blockout.world;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.common.IEvent;
import org.blockout.world.entity.Player;
import org.blockout.world.event.PlayerMoveEvent;
import org.blockout.world.state.IStateMachineListener;

@Named
public class LocalGameState implements IStateMachineListener {

	protected Player	player;

	protected float		currentPlayerX;
	protected float		currentPlayerY;
	protected float		desiredPlayerX;
	protected float		desiredPlayerY;

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

	@Inject
	public LocalGameState(final IWorld world) {

	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer( final Player player ) {
		this.player = player;
	}

	@Override
	public void eventCommitted( final IEvent<?> event ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void eventPushed( final IEvent<?> event ) {
		if ( event instanceof PlayerMoveEvent ) {
			PlayerMoveEvent pme = (PlayerMoveEvent) event;
			desiredPlayerX = pme.getNewX();
			desiredPlayerY = pme.getNewY();
		}
	}

	@Override
	public void eventRolledBack( final IEvent<?> event ) {
		if ( event instanceof PlayerMoveEvent ) {
			PlayerMoveEvent pme = (PlayerMoveEvent) event;
			desiredPlayerX = pme.getNewX();
			desiredPlayerY = pme.getNewY();
		}
	}

}
