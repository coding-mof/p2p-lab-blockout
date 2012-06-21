package org.blockout.ai;

import org.blockout.common.TileCoordinate;
import org.blockout.ui.Camera;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;

import com.google.common.base.Preconditions;

public class AIUtils {
    /**
     * Move the AI to a tile position
     * 
     * @param context
     *            Current {@link AIContext}
     * @param coord
     *            Position where the AI should be moved
     * 
     * @throws NullPointerException
     *             If context or coord are null
     */
    public static void gotoTile( final AIContext context,
            final TileCoordinate coord ) {
        Preconditions.checkNotNull( context );
        Preconditions.checkNotNull( coord );

        Camera localCamera = context.getCamera().getReadOnly();

        float cameraCenterX = localCamera.getCenterX();
        float cameraCenterY = localCamera.getCenterY();

        int tileX = Camera.worldToTile( coord.getX() );
        int tileY = Camera.worldToTile( coord.getY() );
        int centerX = Camera.worldToTile( cameraCenterX );
        int centerY = Camera.worldToTile( cameraCenterY );

        // Handle Movements
        int fromX = centerX - localCamera.getStartTileX();
        int fromY = centerY - localCamera.getStartTileY();
        int toX = tileX - localCamera.getStartTileX();
        int toY = tileY - localCamera.getStartTileY();

        Path path = context.getPathfinder().findPath(
                context.getGameState().getPlayer(), fromX, fromY, toX, toY );
        Path worldPath = new Path();
        if( path != null ) {
            for ( int i = 0; i < path.getLength(); i++ ) {
                Step step = path.getStep( i );
                worldPath.appendStep(
                        step.getX() + localCamera.getStartTileX(), step.getY()
                                + localCamera.getStartTileY() );
            }
        }
        context.getPlayerController().setPath( context.getStateMachine(),
                worldPath );
    }
}
