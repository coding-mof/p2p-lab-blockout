package org.blockout.logic.handler;

import java.util.Random;

import javax.inject.Inject;

import org.blockout.common.TileCoordinate;
import org.blockout.engine.AnimationManager;
import org.blockout.engine.animation.ParticleAnimation;
import org.blockout.engine.animation.effects.SparkleEmitter;
import org.blockout.engine.sfx.AudioType;
import org.blockout.engine.sfx.IAudioManager;
import org.blockout.world.IWorld;
import org.blockout.world.entity.Crate;
import org.blockout.world.entity.Monster;
import org.blockout.world.entity.Player;
import org.blockout.world.event.IEvent;
import org.blockout.world.event.MonsterSlayedEvent;
import org.blockout.world.event.RewardXPEvent;
import org.blockout.world.state.IStateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * This class handles {@link MonsterSlayedEvent}s and spawns items that get
 * dropped.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class MonsterDeathHandler implements IEventHandler {

    private static final Logger logger;
    static {
        logger = LoggerFactory.getLogger( MonsterDeathHandler.class );
    }
    protected IWorld            world;
    private final Random        rand;

    private final AudioType[]   dyingSounds;
    private IAudioManager       audioManager;

    private AnimationManager    animationManager;

    @Inject
    public MonsterDeathHandler( final IWorld world ) {
        Preconditions.checkNotNull( world );

        this.world = world;
        rand = new Random( System.currentTimeMillis() );

        dyingSounds = new AudioType[] { AudioType.sfx_monster_death1,
                AudioType.sfx_monster_death2, AudioType.sfx_monster_death3 };
    }

    public void setAudioManager( final IAudioManager audioManager ) {
        this.audioManager = audioManager;
    }

    public void setAnimationManager( AnimationManager animationManager ) {
        this.animationManager = animationManager;
    }

    @Override
    public void eventStarted( final IStateMachine stateMachine,
            final IEvent<?> event ) {
    }

    @Override
    public void eventFinished( final IStateMachine stateMachine,
            final IEvent<?> event ) {
        if( !( event instanceof MonsterSlayedEvent ) ) {
            return;
        }

        MonsterSlayedEvent mse = (MonsterSlayedEvent) event;

        if( audioManager != null ) {
            int index = rand.nextInt( dyingSounds.length );
            audioManager.getSound( dyingSounds[index] ).play();
        }
        if( mse.getActor() instanceof Player ) {

            // activate player object
            Player player = world.findEntity( mse.getActor().getId(),
                    Player.class );

            RewardXPEvent xpEvent = new RewardXPEvent( player, 75 );
            stateMachine.pushEvent( xpEvent );
        }

        // activate monster object
        Monster monster = world.findEntity( mse.getMonster().getId(),
                Monster.class );

        TileCoordinate coordinate = world.findTile( monster );
        if( coordinate != null ) {
            world.removeEntity( monster );

            int porbability = 60;
            logger.info( porbability + "% probability to spawn a crate..." );
            if( rand.nextInt( 100 ) < porbability ) {
                logger.info( "Spawning crate on old monster position "
                        + coordinate );
                world.setEnityPosition( new Crate(), coordinate );
                ParticleAnimation animation = new ParticleAnimation();
                animation.addEffect( "sparkles", new SparkleEmitter( 3500 ) );
                animationManager.addAnimation( animation, coordinate );
            }
        } else {
            logger.warn( "Monster " + monster + " no longer exists." );
        }
    }
}
