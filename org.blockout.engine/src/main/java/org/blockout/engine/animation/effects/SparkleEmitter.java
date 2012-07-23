package org.blockout.engine.animation.effects;

import java.util.Random;

import org.blockout.engine.ISpriteSheet;
import org.blockout.engine.SpriteSheetImpl;
import org.newdawn.slick.Image;
import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;

public class SparkleEmitter implements ParticleEmitter {

    ISpriteSheet spriteSheet = null;
    Random       rand;
    int          playTime;
    int          time;
    int          nextParticle;

    public SparkleEmitter( final int playTime ) {
        this.playTime = playTime;
        rand = new Random( System.currentTimeMillis() );
        time = this.playTime;
    }

    @Override
    public void update( ParticleSystem system, int delta ) {
        time -= delta;
        nextParticle -= delta;

        if( 0 > nextParticle ) {
            nextParticle = 50;
            Particle p = system.getNewParticle( this, 1000 );
            p.setPosition( 16 - rand.nextInt( 32 ), 16 - rand.nextInt( 32 ) );
            p.setSize( 8 );
            p.setImage( getRandomSparkle() );
        }
    }

    @Override
    public boolean completed() {
        return 0 > time;
    }

    @Override
    public void wrapUp() {
    }

    @Override
    public void updateParticle( Particle particle, int delta ) {
        float c = 0.002f * delta;
        particle.adjustColor( 0, -c / 2, -c * 2, -c / 4 );
        particle.adjustSize( 0.01f * delta );
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
        return getRandomSparkle();
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
        time = playTime;
    }

    private Image getRandomSparkle() {
        if( null == spriteSheet ) {
            try {
                spriteSheet = new SpriteSheetImpl( "sparkle.png", 8, 8 );
            } catch ( Exception e ) {
                throw new RuntimeException( e );
            }
        }

        int index = rand.nextInt( spriteSheet.getSpriteCount() );
        return spriteSheet.getSprite( index );
    }
}
