package org.blockout.logic.handler;

import javax.inject.Inject;

import org.blockout.world.IWorld;
import org.blockout.world.entity.Player;
import org.blockout.world.event.IEvent;
import org.blockout.world.event.RewardXPEvent;
import org.blockout.world.state.IStateMachine;

/**
 * This handler applies the rewarded experience to the player.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class RewardXPHandler implements IEventHandler {

	protected IWorld	world;

	@Inject
	public RewardXPHandler(final IWorld world) {
		this.world = world;
	}

	@Override
	public void eventStarted( final IStateMachine stateMachine, final IEvent<?> event ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void eventFinished( final IStateMachine stateMachine, final IEvent<?> event ) {
		if ( !(event instanceof RewardXPEvent) ) {
			return;
		}

		RewardXPEvent xpe = (RewardXPEvent) event;

		// activate player object
		Player player = world.findEntity( xpe.getPlayer().getId(), Player.class );

		player.rewardXP( xpe.getExperience() );
	}

}
