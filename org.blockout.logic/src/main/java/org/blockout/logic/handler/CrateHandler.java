package org.blockout.logic.handler;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.engine.sfx.AudioType;
import org.blockout.engine.sfx.IAudioManager;
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

	protected IAudioManager		audioManager;

	@Inject
	public CrateHandler(final IAudioManager audioManager) {
		this.audioManager = audioManager;
	}

	@Override
	public void eventStarted( final IStateMachine stateMachine, final IEvent<?> event ) {
		if ( !(event instanceof CrateOpenedEvent) ) {
			return;
		}
		audioManager.getSound( AudioType.sfx_open_chest ).play();
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
