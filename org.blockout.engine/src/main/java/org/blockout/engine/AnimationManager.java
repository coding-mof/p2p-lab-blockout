package org.blockout.engine;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.common.TileCoordinate;
import org.blockout.engine.animation.IAnimation;
import org.blockout.engine.animation.ParticleAnimation;
import org.blockout.engine.animation.effects.ExplosionEmitter;
import org.newdawn.slick.Graphics;

import com.google.common.base.Preconditions;

@Named
public class AnimationManager {

    private static class AnimationEntry {
        public IAnimation     animation;
        public TileCoordinate position;

        public AnimationEntry( IAnimation animation, TileCoordinate position ) {
            this.animation = animation;
            this.position = position;
        }
    }

    public final ReadWriteLock   rwLock    = new ReentrantReadWriteLock( true );
    public final Lock            readLock  = rwLock.readLock();
    public final Lock            writeLock = rwLock.writeLock();

    private List<AnimationEntry> animationList;
    private Camera               camera;

    private ParticleAnimation    animation;

    @Inject
    public AnimationManager( final Camera camera ) {
        this.animationList = new LinkedList<AnimationEntry>();
        this.camera = camera;

        // new Thread( new Runnable() {
        // @Override
        // public void run() {
        // while(true){
        // readLock.lock();
        // try {
        // animationList = new LinkedList<AnimationManager.AnimationEntry>(
        // Collections2
        // .filter(
        // animationList,
        // new Predicate<AnimationEntry>() {
        // @Override
        // public boolean apply(
        // AnimationEntry arg ) {
        // return arg.animation
        // .completed();
        // }
        // } ) );
        //
        // } finally {
        // readLock.unlock();
        // }
        //
        // try {
        // Thread.sleep( 1000 );
        // } catch ( InterruptedException e ) {
        // Thread.currentThread().interrupt();
        // }
        //
        // if(Thread.interrupted()){
        // return;
        // }
        // }
        // }
        // } ).start();
    }

    public void addAnimation( final IAnimation animation,
            final TileCoordinate position ) {
        Preconditions.checkNotNull( animation );
        Preconditions.checkNotNull( position );

        writeLock.lock();
        try {
            animation.start();
            animationList.add( new AnimationEntry( animation, position ) );
        } finally {
            writeLock.unlock();
        }
    }

    public void update( final long delta ) {
        readLock.lock();
        try {
            for ( AnimationEntry entry : animationList ) {
                entry.animation.update( delta );
            }

            getParticelAnimation().update( delta );
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }

    public void render( final Graphics g ) {
        readLock.lock();
        try {
            for ( AnimationEntry entry : animationList ) {
                entry.animation.render( 100, 100 );
            }

            getParticelAnimation().render( 100, 100 );
        } finally {
            readLock.unlock();
        }
    }

    private IAnimation getParticelAnimation() {
        if( null == animation ) {
            animation = new ParticleAnimation();
            animation.addEffect( "bla", new ExplosionEmitter( 5 ) );
            animation.setLooping( true );
            animation.start();
        }

        return animation;
    }
}
