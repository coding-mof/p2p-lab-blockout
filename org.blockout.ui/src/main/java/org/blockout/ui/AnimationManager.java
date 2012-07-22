package org.blockout.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.common.TileCoordinate;
import org.blockout.engine.animation.IAnimation;
import org.newdawn.slick.Graphics;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

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

    public final ReadWriteLock rwLock    = new ReentrantReadWriteLock( true );
    public final Lock          readLock  = rwLock.readLock();
    public final Lock          writeLock = rwLock.writeLock();
    
    private final Camera       camera;
    private List<AnimationEntry> animationList;

    @Inject
    public AnimationManager( final Camera camera ) {
        this.camera = camera;
        this.animationList = new LinkedList<AnimationEntry>();
        
        new Thread( new Runnable() {
            @Override
            public void run() {
                while(true){
                    readLock.lock();
                    try {
                        animationList = new LinkedList<AnimationManager.AnimationEntry>(
                                Collections2
                                .filter(
                                        animationList,
                                        new Predicate<AnimationEntry>() {
                                            @Override
                                            public boolean apply(
                                                    AnimationEntry arg ) {
                                                return arg.animation
                                                        .completed();
                                            }
                                        } ) );

                    } finally {
                        readLock.unlock();
                    }
                    
                    try {
                        Thread.sleep( 1000 );
                    } catch ( InterruptedException e ) {
                        Thread.currentThread().interrupt();
                    }
                    
                    if(Thread.interrupted()){
                        return;
                    }
                }
            }
        } ).start();
    }
    
    public void addAnimation( final IAnimation animation,
            final TileCoordinate position ) {

        writeLock.lock();
        try {
            animation.start();
            animation.setLooping( true );
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
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }

    public void render( final Graphics g ) {
        Camera localCamera = camera.getReadOnly();

        // int tileSize = localCamera.getTileSize();
        // int startTileX = localCamera.getStartTileX();
        // int startTileY = localCamera.getStartTileY();
        
        readLock.lock();
        try {
            for ( AnimationEntry entry : animationList ) {
                if(localCamera.isInFrustum( entry.position )){
                    entry.animation.render( 0, 0 );
                }
            }
        } finally {
            readLock.unlock();
        }
    }
}
