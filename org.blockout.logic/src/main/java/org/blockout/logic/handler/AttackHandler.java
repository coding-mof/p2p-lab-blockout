package org.blockout.logic.handler;

import java.util.ArrayList;
import java.util.Random;

import javax.inject.Inject;

import org.blockout.common.TileCoordinate;
import org.blockout.engine.AnimationManager;
import org.blockout.engine.animation.ParticleAnimation;
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
import org.newdawn.slick.particles.effects.FireEmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the logic for handling attacks between {@link Actor}s.
 * 
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class AttackHandler implements IEventHandler {

    private static final Logger        logger;
    static {
        logger = LoggerFactory.getLogger( AttackHandler.class );
    }

    protected IAudioManager            audioManager;
    protected IWorld                   world;

    private final ArrayList<AudioType> attackTypes;
    private final Random               rand;

    private AnimationManager           animationManager;

    @Inject
    public AttackHandler( final IWorld world ) {
        attackTypes = new ArrayList<AudioType>();
        attackTypes.add( AudioType.sfx_sword_clash1 );
        attackTypes.add( AudioType.sfx_sword_clash2 );
        attackTypes.add( AudioType.sfx_sword_clash3 );
        attackTypes.add( AudioType.sfx_sword_clash4 );
        attackTypes.add( AudioType.sfx_sword_clash5 );
        rand = new Random( System.currentTimeMillis() );
        this.world = world;
    }

    public void setAudioManager( final IAudioManager audioManager ) {
        this.audioManager = audioManager;
    }

    public void setAnimationManager( final AnimationManager animationManager ) {
        this.animationManager = animationManager;
    }

    @Override
    public void eventStarted( final IStateMachine stateMachine,
            final IEvent<?> event ) {
        if( !( event instanceof AttackEvent ) ) {
            return;
        }

        if( audioManager != null ) {
            int index = rand.nextInt( attackTypes.size() );
            audioManager.getSound( attackTypes.get( index ) ).play();
        }
    }

    @Override
    public void eventFinished( final IStateMachine stateMachine,
            final IEvent<?> event ) {
        if( !( event instanceof AttackEvent ) ) {
            return;
        }

        AttackEvent ae = (AttackEvent) event;

        ParticleAnimation animation = new ParticleAnimation();
        animation.addEffect( "fire", new FireEmitter() );
        animationManager.addAnimation( animation, null );

        // activate objects
        Actor aggressor = world.findEntity( ae.getAggressor().getId(),
                Actor.class );
        Actor victim = world.findEntity( ae.getVictim().getId(), Actor.class );

        int damage = aggressor.getStrength() - victim.getArmor();
        if( damage > 0 ) {
            logger.debug( "Doing " + damage + " damage to victim." );
            int healthLeft = victim.getCurrentHealth() - damage;
            if( healthLeft <= 0 ) {
                TileCoordinate victimCoord = world.findTile( victim );
                if( ae.getVictim() instanceof Player ) {
                    logger.info( "Player died: " + victim );
                    stateMachine.pushEvent( new PlayerDiedEvent(
                            (Player) victim, victimCoord ) );
                } else if( victim instanceof Monster ) {
                    logger.info( "Monster died: " + victim );
                    stateMachine.pushEvent( new MonsterSlayedEvent( aggressor,
                            (Monster) victim, victimCoord ) );
                }
            } else {
                victim.setCurrentHealth( healthLeft );
            }
        }
    }
}
