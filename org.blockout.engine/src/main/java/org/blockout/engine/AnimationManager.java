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

/**
 * The AnimationManager handles all that are started inside the game
 * 
 * @author Florian Müller
 * 
 */
public class AnimationManager {
    private static class AnimationEntry {
        public IAnimation     animation;
        public TileCoordinate position;

        public AnimationEntry( IAnimation animation, TileCoordinate position ) {
            this.animation = animation;
            this.position = position;
        }
    }

    private final ReadWriteLock  rwLock    = new ReentrantReadWriteLock( true );
    private final Lock           readLock  = rwLock.readLock();
    private final Lock           writeLock = rwLock.writeLock();

    private List<AnimationEntry> animationList;
    private Camera               camera;

    /**
     * Constructor to create a AnimationManager
     * 
     * @param camera
     *            Camera to render animation properly
     * 
     * @throws NullPointerException
     *             Thrown if the argument was null
     */
    @Inject
    public AnimationManager( final Camera camera ) {
        Preconditions.checkNotNull( camera, "camera is null" );

        this.animationList = new LinkedList<AnimationEntry>();
        this.camera = camera;
    }

    /**
     * Add a new animation to the AnimationManager
     * 
     * @param animation
     *            The animation
     * @param position
     *            On wich tile should the aniomation be rendered
     * @throws NullPointerException
     *             Thrown if an argument is null
     */
    public void addAnimation( final IAnimation animation,
            final TileCoordinate position ) {
        Preconditions.checkNotNull( animation, "animation is null" );
        Preconditions.checkNotNull( position, "position is null" );

        writeLock.lock();
        try {
            animation.start();
            animationList.add( new AnimationEntry( animation, position ) );
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Update the state of all active animations
     * 
     * @param delta
     *            Time between two calls of update
     */
    public void update( final long delta ) {
        readLock.lock();
        try {
            // remove all completed animations
            List<AnimationEntry> removeMe = new LinkedList<AnimationEntry>();
            for ( AnimationEntry entry : animationList ) {
                IAnimation animation = entry.animation;
                if( animation.completed() && !animation.isLooping() ) {
                    removeMe.add( entry );
                }
            }
            animationList.removeAll( removeMe );

            for ( AnimationEntry entry : animationList ) {
                IAnimation animation = entry.animation;
                animation.update( delta );
            }
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Render all active animations
     * 
     * @param g
     *            Current graphic context
     */
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
                    animation.render( x,
                            localCam.convertY( y )
                            - localCam.getTileSize() );
                }
            }
        } finally {
            readLock.unlock();
        }
    }
}
