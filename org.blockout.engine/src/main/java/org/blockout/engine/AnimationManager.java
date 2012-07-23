package org.blockout.engine;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.inject.Inject;

import org.blockout.common.TileCoordinate;
import org.blockout.engine.animation.IAnimation;
import org.newdawn.slick.Graphics;

import com.google.common.base.Preconditions;

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

    @Inject
    public AnimationManager( final Camera camera ) {
        this.animationList = new LinkedList<AnimationEntry>();
        this.camera = camera;
    }

    public void addAnimation( final IAnimation animation,
            final TileCoordinate position ) {
        Preconditions.checkNotNull( animation );
        Preconditions.checkNotNull( position );


        writeLock.lock();
        try {
            animation.start();
            System.err.println( animation );
            animationList.add( new AnimationEntry( animation, position ) );

        } finally {
            writeLock.unlock();
        }

        for ( AnimationEntry entry : animationList ) {
            System.err.println( "inlist: " + entry.animation );
        }
    }

    public void update( final long delta ) {
        readLock.lock();

        try {

            List<AnimationEntry> removeMe = new LinkedList<AnimationEntry>();
            for ( AnimationEntry entry : animationList ) {
                if( entry.animation.completed() ){
                    System.err.println( "remove: " + entry.animation );
                    removeMe.add( entry );
                }
            }
            animationList.removeAll( removeMe );
            // System.err.println( "update " + animationList.size() );

            for ( AnimationEntry entry : animationList ) {
                System.err.println( "update: " + entry.animation );
                entry.animation.update( delta );
            }
        } finally {
            readLock.unlock();
        }
    }

    public void render( final Graphics g ) {
        Camera localCam = camera.getReadOnly();
        int tileSize = localCam.getTileSize();

        readLock.lock();
        try {
            for ( AnimationEntry entry : animationList ) {
                TileCoordinate position = entry.position;
                IAnimation animation = entry.animation;

                if( localCam.isInFrustum( position ) ) {
                    int tileRelX = position.getX() - localCam.getStartTileX();
                    int tileRelY = position.getY() - localCam.getStartTileY();
                    
                    int x = -localCam.getWidthOffset()
                            + ( tileRelX * localCam.getTileSize() )
                            + ( tileSize / 2 );
                    int y = -localCam.getHeightOffset()
                            + ( tileRelY * localCam.getTileSize() )
                            - ( tileSize / 2 );
                    System.err.println( "render: " + entry.animation );
                    entry.animation.render( x, localCam.convertY( y )
                            - localCam.getTileSize() );
                }
            }
        } finally {
            readLock.unlock();
        }
    }
}
