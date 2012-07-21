package org.blockout.logic.handler;

import java.util.Random;

import javax.inject.Inject;

import org.blockout.common.TileCoordinate;
import org.blockout.engine.sfx.AudioType;
import org.blockout.engine.sfx.IAudioManager;
import org.blockout.world.IWorld;
import org.blockout.world.entity.Player;
import org.blockout.world.event.IEvent;
import org.blockout.world.event.PlayerDiedEvent;
import org.blockout.world.state.IStateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class PlayerDeathHandler implements IEventHandler {

	private static final Logger	logger;
	static {
		logger = LoggerFactory.getLogger( PlayerDeathHandler.class );
	}
	protected IWorld			world;
	private final Random		rand;

	private final AudioType[]	dyingSounds;
	private IAudioManager		audioManager;

	@Inject
	public PlayerDeathHandler(final IWorld world) {

		Preconditions.checkNotNull( world );

		this.world = world;
		rand = new Random( System.currentTimeMillis() );

		dyingSounds = new AudioType[] { AudioType.sfx_player_death1, AudioType.sfx_player_death2,
				AudioType.sfx_player_death3 };
	}

	public void setAudioManager( final IAudioManager audioManager ) {
		this.audioManager = audioManager;
	}

	@Override
	public void eventStarted( final IStateMachine stateMachine, final IEvent<?> event ) {
		// TODO: play monster death sound
	}

	@Override
	public void eventFinished( final IStateMachine stateMachine, final IEvent<?> event ) {
		if ( !(event instanceof PlayerDiedEvent) ) {
			return;
		}

		PlayerDiedEvent evt = (PlayerDiedEvent) event;
		if ( audioManager != null ) {
			int index = rand.nextInt( dyingSounds.length );
			audioManager.getSound( dyingSounds[index] ).play();
		}

		// activate player object
		Player player = world.findEntity( evt.getPlayer().getId(), Player.class );
		player.setExperience( 0 );
		player.setCurrentHealth( player.getMaxHealth() );
		logger.info( "Player " + player + " died. Respawning player at (1,1)." );
		world.setEnityPosition( player, new TileCoordinate( 1, 1 ) );
	}
}
