package org.blockout.world;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.common.IEvent;
import org.blockout.world.state.IStateMachineListener;

@Named
public class LocalGameState implements IStateMachineListener {

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

	@Override
	public void eventCommitted( final IEvent event ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void performEvent( final IEvent event ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void undoEvent( final IEvent event ) {
		// TODO Auto-generated method stub

	}

}
