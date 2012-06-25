package org.blockout.logic.handler;

import javax.inject.Inject;

import org.blockout.engine.sfx.AudioType;
import org.blockout.engine.sfx.IAudioManager;
import org.blockout.world.IWorld;
import org.blockout.world.entity.Crate;
import org.blockout.world.entity.Player;
import org.blockout.world.event.CrateOpenedEvent;
import org.blockout.world.event.IEvent;
import org.blockout.world.items.Item;
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
public class CrateHandler implements IEventHandler {

	private static final Logger	logger;
	static {
		logger = LoggerFactory.getLogger( CrateHandler.class );
	}

	protected IAudioManager		audioManager;
	protected IWorld			world;

	@Inject
	public CrateHandler(final IWorld world) {
		this.world = world;
	}

	public void setAudioManager( final IAudioManager audioManager ) {
		this.audioManager = audioManager;
	}

	@Override
	public void eventStarted( final IStateMachine stateMachine, final IEvent<?> event ) {
		if ( !(event instanceof CrateOpenedEvent) ) {
			return;
		}
		if ( audioManager != null ) {
			audioManager.getSound( AudioType.sfx_open_chest ).play();
		}
	}

	@Override
	public void eventFinished( final IStateMachine stateMachine, final IEvent<?> event ) {
		if ( !(event instanceof CrateOpenedEvent) ) {
			return;
		}
		CrateOpenedEvent coa = (CrateOpenedEvent) event;

		Crate crate = world.findEntity( coa.getCrate().getId(), Crate.class );
		Player player = world.findEntity( coa.getPlayer().getId(), Player.class );

		logger.info( "Crate " + crate + " opened by " + player );
		Item item = crate.getItem();
		if ( item == null ) {
			logger.info( "Crate " + crate + " was empty." );
		} else {
			if ( player.getInventory().storeItem( item ) ) {
				crate.removeItem();
				logger.info( "Player " + player + " got " + item );
			} else {
				logger.info( "Player " + player + " has no free slot in his inventory." );
			}
		}
	}
}
