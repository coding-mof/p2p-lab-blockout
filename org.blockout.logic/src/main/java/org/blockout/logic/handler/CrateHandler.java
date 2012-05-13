package org.blockout.logic.handler;

import javax.inject.Named;

import org.blockout.world.event.CrateOpenedEvent;
import org.blockout.world.event.IEvent;
import org.blockout.world.state.IStateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the logic for handling {@link CrateOpenedEvent}s. It's
 * responsible for triggering a sound and dropping the items which are in the
 * crate.
 * 
 * @author Marc-Christian Schulze
 * 
 */
@Named
public class CrateHandler implements IEventHandler {

	private static final Logger	logger;
	static {
		logger = LoggerFactory.getLogger( CrateHandler.class );
	}

	@Override
	public void eventStarted( final IStateMachine stateMachine, final IEvent<?> event ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void eventFinished( final IStateMachine stateMachine, final IEvent<?> event ) {
		if ( !(event instanceof CrateOpenedEvent) ) {
			return;
		}
		CrateOpenedEvent coa = (CrateOpenedEvent) event;

		logger.info( "Crate " + coa.getCrate() + " opened by " + coa.getPlayer() );
	}
}
