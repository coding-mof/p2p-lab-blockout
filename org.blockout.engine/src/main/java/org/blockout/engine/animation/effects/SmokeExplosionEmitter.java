package org.blockout.engine.animation.effects;

import java.util.Random;

import org.blockout.engine.ISpriteSheet;
import org.blockout.engine.SpriteSheetImpl;
import org.newdawn.slick.Image;
import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;

public class SmokeExplosionEmitter implements ParticleEmitter {

    ISpriteSheet spriteSheet;
    Random       rand;

    int          time;
    boolean      started;
    int          numSmokeBalls;

    public SmokeExplosionEmitter( final int numSmokeBalls ) {
        this.numSmokeBalls = numSmokeBalls;
        this.rand = new Random();
        this.started = false;
    }

    @Override
    public void update( ParticleSystem system, int delta ) {
        time -= delta;

        if( !started ) {
            started = true;
            time = 2000;

            for ( int i = 0; i < numSmokeBalls; i++ ) {
                Particle p = system.getNewParticle( this, 1000 );
                p.setPosition( 16 - rand.nextInt( 32 ), 16 - rand.nextInt( 32 ) );
                p.setSize( 13 );
                p.setVelocity( -.5f + rand.nextFloat(),
                        -.5f + rand.nextFloat(), .05f );
                p.setImage( getRandomSmokeBall() );
            }
        }
    }

    @Override
    public void updateParticle( Particle particle, int delta ) {
        float c = 0.002f * delta;
        particle.adjustColor( -c / 3, -c / 3, -c / 3, -c / 4 );
        particle.adjustSize( 0.01f * delta );
    }

    @Override
    public boolean completed() {
        return 0 > time;
    }

    @Override
    public void wrapUp() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void setEnabled( boolean enabled ) {
    }

    @Override
    public boolean useAdditive() {
        return false;
    }

    @Override
    public Image getImage() {
        return getRandomSmokeBall();
    }

    @Override
    public boolean isOriented() {
        return false;
    }

    @Override
    public boolean usePoints( ParticleSystem system ) {
        return false;
    }

    @Override
    public void resetState() {
        started = false;
    }

    private Image getRandomSmokeBall() {
        if( null == spriteSheet ) {
            try {
                spriteSheet = new SpriteSheetImpl( "smoke.png", 16, 16 );
            } catch ( Exception e ) {
                throw new RuntimeException( e );
            }
        }

        int index = rand.nextInt( spriteSheet.getSpriteCount() );
        return spriteSheet.getSprite( index );
    }
}
