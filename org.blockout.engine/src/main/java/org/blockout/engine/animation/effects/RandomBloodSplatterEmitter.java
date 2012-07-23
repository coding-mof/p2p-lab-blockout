package org.blockout.engine.animation.effects;

import java.util.Random;

import org.blockout.engine.ISpriteSheet;
import org.blockout.engine.SpriteSheetImpl;
import org.newdawn.slick.Image;
import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;

public class RandomBloodSplatterEmitter implements ParticleEmitter {

    ISpriteSheet spriteSheet = null;
    Image        currentImg;
    Random       rand;
    boolean      started;
    long         time;

    public RandomBloodSplatterEmitter() {
        rand = new Random( System.currentTimeMillis() );
        started = false;
        time = Long.MAX_VALUE;
    }

    @Override
    public void update( ParticleSystem system, int delta ) {
        time -= delta;

        if( !started ) {
            started = true;
            time = 1000;

            Particle p = system.getNewParticle( this, 1000 );
            p.setSize( 32 );
            p.setImage( getImage() );
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
        particle.adjustColor( 0, 0, 0, -c / 2 );
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
        if( null == currentImg ) {
            currentImg = getRandomBlood();
        }

        return currentImg;
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
        currentImg = null;
    }

    private Image getRandomBlood() {
        if( null == spriteSheet ) {
            try {
                spriteSheet = new SpriteSheetImpl( "blood_splatter.png", 32, 32 );
            } catch ( Exception e ) {
                throw new RuntimeException( e );
            }
        }

        int index = rand.nextInt( spriteSheet.getSpriteCount() );
        return spriteSheet.getSprite( index );
    }
}
