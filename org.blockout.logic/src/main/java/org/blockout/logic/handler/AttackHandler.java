package org.blockout.logic.handler;

import java.util.ArrayList;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.common.TileCoordinate;
import org.blockout.engine.sfx.AudioType;
import org.blockout.engine.sfx.IAudioManager;
import org.blockout.world.IWorld;
import org.blockout.world.entity.Actor;
import org.blockout.world.entity.Monster;
import org.blockout.world.entity.Player;
import org.blockout.world.event.AttackEvent;
import org.blockout.world.event.IEvent;
import org.blockout.world.event.MonsterSlayedEvent;
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

	private static final Logger			logger;
	static {
		logger = LoggerFactory.getLogger( AttackHandler.class );
	}

	protected IAudioManager				audioManager;
	protected IWorld					world;

	private final ArrayList<AudioType>	attackTypes;
	private final Random				rand;

	@Inject
	public AttackHandler(final IAudioManager audioManager, final IWorld world) {
		this.audioManager = audioManager;
		attackTypes = new ArrayList<AudioType>();
		attackTypes.add( AudioType.sfx_sword_clash1 );
		attackTypes.add( AudioType.sfx_sword_clash2 );
		attackTypes.add( AudioType.sfx_sword_clash3 );
		attackTypes.add( AudioType.sfx_sword_clash4 );
		attackTypes.add( AudioType.sfx_sword_clash5 );
		rand = new Random( System.currentTimeMillis() );
		this.world = world;
	}

	@Override
	public void eventStarted( final IStateMachine stateMachine, final IEvent<?> event ) {
		if ( !(event instanceof AttackEvent) ) {
			return;
		}

		int index = rand.nextInt( attackTypes.size() );
		audioManager.getSound( attackTypes.get( index ) ).play();
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
				TileCoordinate victimCoord = world.findTile( ae.getVictim() );
				if ( ae.getVictim() instanceof Player ) {
					logger.info( "Player died: " + ae.getVictim() );
					stateMachine.pushEvent( new PlayerDiedEvent( (Player) ae.getVictim(), victimCoord ) );
				} else if ( ae.getVictim() instanceof Monster ) {
					logger.info( "Monster died: " + ae.getVictim() );
					stateMachine.pushEvent( new MonsterSlayedEvent( ae.getAggressor(), (Monster) ae.getVictim(),
							victimCoord ) );
				}
			} else {
				ae.getVictim().setCurrentHealth( healthLeft );
			}
		}
	}
}
