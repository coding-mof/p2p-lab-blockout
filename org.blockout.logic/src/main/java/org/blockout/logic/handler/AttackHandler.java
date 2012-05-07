package org.blockout.logic.handler;

import javax.inject.Named;

import org.blockout.common.IEvent;
import org.blockout.world.event.AttackEvent;
import org.blockout.world.state.IStateMachine;

@Named
public class AttackHandler implements IEventHandler {

	@Override
	public void eventStarted( final IStateMachine stateMachine, final IEvent<?> event ) {
		if ( !(event instanceof AttackEvent) ) {
			return;
		}

		AttackEvent ae = (AttackEvent) event;

		ae.getVictim().setCurrentHealth( ae.getVictim().getCurrentHealth() - 15 );
		ae.getAggressor().setCurrentHealth( ae.getAggressor().getCurrentHealth() - 5 );
	}

	@Override
	public void eventFinished( final IStateMachine stateMachine, final IEvent<?> event ) {
		// TODO Auto-generated method stub

	}

}
