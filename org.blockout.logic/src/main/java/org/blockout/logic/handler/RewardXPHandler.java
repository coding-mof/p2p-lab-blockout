package org.blockout.logic.handler;

import javax.inject.Named;

import org.blockout.world.event.IEvent;
import org.blockout.world.event.RewardXPEvent;
import org.blockout.world.state.IStateMachine;

@Named
public class RewardXPHandler implements IEventHandler {

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
		xpe.getPlayer().rewardXP( xpe.getExperience() );

	}

}
