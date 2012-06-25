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
	public static boolean gotoTile( final AIContext context, final TileCoordinate coord ) {
		Preconditions.checkNotNull( context );
		Preconditions.checkNotNull( coord );

		Path worldPath = findPathTo( context, coord );
		if ( worldPath == null ) {
			return false;
		}
		context.getPlayerController().setPath( context.getStateMachine(), worldPath );
		return true;
	}

	private static Path findPathTo( final AIContext context, final TileCoordinate coord ) {
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

		Path path = context.getPathfinder().findPath( context.getGameState().getPlayer(), fromX, fromY, toX, toY );
		if ( path == null || path.getLength() == 0 ) {
			return null;
		}
		Path worldPath = new Path();
		if ( path != null ) {
			for ( int i = 0; i < path.getLength(); i++ ) {
				Step step = path.getStep( i );
				worldPath.appendStep( step.getX() + localCamera.getStartTileX(),
						step.getY() + localCamera.getStartTileY() );
			}
		}
		return worldPath;
	}

	public static TileCoordinate findWalkableTileNextTo( final AIContext context, final TileCoordinate coord ) {
		Path path;
		TileCoordinate neighbourTile;

		Camera camera = context.getCamera().getReadOnly();

		neighbourTile = coord.plus( 1, 1 );
		if ( camera.isInFrustum( neighbourTile ) ) {
			path = findPathTo( context, neighbourTile );
			if ( path != null ) {
				return neighbourTile;
			}
		}

		neighbourTile = coord.plus( 1, 0 );
		if ( camera.isInFrustum( neighbourTile ) ) {
			path = findPathTo( context, neighbourTile );
			if ( path != null ) {
				return neighbourTile;
			}
		}

		neighbourTile = coord.plus( 1, -1 );
		if ( camera.isInFrustum( neighbourTile ) ) {
			path = findPathTo( context, neighbourTile );
			if ( path != null ) {
				return neighbourTile;
			}
		}

		neighbourTile = coord.plus( -1, 1 );
		if ( camera.isInFrustum( neighbourTile ) ) {
			path = findPathTo( context, neighbourTile );
			if ( path != null ) {
				return neighbourTile;
			}
		}

		neighbourTile = coord.plus( -1, 0 );
		if ( camera.isInFrustum( neighbourTile ) ) {
			path = findPathTo( context, neighbourTile );
			if ( path != null ) {
				return neighbourTile;
			}
		}

		neighbourTile = coord.plus( -1, -1 );

		if ( camera.isInFrustum( neighbourTile ) ) {
			path = findPathTo( context, neighbourTile );
			if ( path != null ) {
				return neighbourTile;
			}
		}

		neighbourTile = coord.plus( 0, 1 );
		if ( camera.isInFrustum( neighbourTile ) ) {
			path = findPathTo( context, neighbourTile );
			if ( path != null ) {
				return neighbourTile;
			}
		}

		neighbourTile = coord.plus( 0, -1 );
		if ( camera.isInFrustum( neighbourTile ) ) {
			path = findPathTo( context, neighbourTile );
			if ( path != null ) {
				return neighbourTile;
			}
		}

		return null;
	}
}
