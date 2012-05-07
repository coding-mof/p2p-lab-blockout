package org.blockout.logic.handler;

import javax.inject.Named;

import org.blockout.common.IEvent;
import org.blockout.world.entity.Actor;
import org.blockout.world.entity.Monster;
import org.blockout.world.entity.Player;
import org.blockout.world.event.AttackEvent;
import org.blockout.world.event.PlayerDiedEvent;
import org.blockout.world.state.IStateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the logic for handling attacks between {@link Actor}s.
 * 
 * 
 * @author Marc-Christian Schulze
 * 
 */
@Named
public class AttackHandler implements IEventHandler {

	private static final Logger	logger;
	static {
		logger = LoggerFactory.getLogger( AttackHandler.class );
	}

	@Override
	public void eventStarted( final IStateMachine stateMachine, final IEvent<?> event ) {
	}

	@Override
	public void eventFinished( final IStateMachine stateMachine, final IEvent<?> event ) {
		if ( !(event instanceof AttackEvent) ) {
			return;
		}

		AttackEvent ae = (AttackEvent) event;

		int damage = ae.getAggressor().getStrength() - ae.getVictim().getArmor();
		if ( damage > 0 ) {
			logger.debug( "Doing " + damage + " damage to victim." );
			int healthLeft = ae.getVictim().getCurrentHealth() - damage;
			if ( healthLeft <= 0 ) {
				logger.info( "Actor (Victim) died: " + ae.getVictim() );
				raiseDeathEvent( stateMachine, ae.getVictim() );
			} else {
				ae.getVictim().setCurrentHealth( healthLeft );
			}
		}

		damage = ae.getVictim().getStrength() - ae.getAggressor().getArmor();
		if ( damage > 0 ) {
			logger.debug( "Doing " + damage + " aggressor to victim." );
			int healthLeft = ae.getAggressor().getCurrentHealth() - damage;
			if ( healthLeft <= 0 ) {
				logger.info( "Actor (Aggressor) died: " + ae.getAggressor() );
				raiseDeathEvent( stateMachine, ae.getAggressor() );
			} else {
				ae.getAggressor().setCurrentHealth( healthLeft );
			}
		}
	}

	private void raiseDeathEvent( final IStateMachine stateMachine, final Actor actor ) {
		if ( actor instanceof Player ) {
			stateMachine.pushEvent( new PlayerDiedEvent( (Player) actor ) );
		} else if ( actor instanceof Monster ) {

		}
	}
}
