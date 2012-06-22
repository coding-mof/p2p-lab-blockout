package org.blockout.ai;

import javax.inject.Inject;

import org.blockout.common.TileCoordinate;
import org.blockout.ui.Camera;
import org.blockout.ui.LocalPlayerMoveHandler;
import org.blockout.world.IWorld;
import org.blockout.world.LocalGameState;
import org.blockout.world.Tile;
import org.blockout.world.entity.Actor;
import org.blockout.world.entity.Crate;
import org.blockout.world.entity.Entity;
import org.blockout.world.state.IStateMachine;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;
import org.newdawn.slick.util.pathfinding.PathFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * Implementation of a simple artificial intelligence (AI). It searches the
 * terrain for enemies and crates.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class SimpleAIPlayer implements AIPlayer {

	private static final Logger				logger;
	static {
		logger = LoggerFactory.getLogger( SimpleAIPlayer.class );
	}

	private final IWorld					world;
	private final LocalGameState			gameState;
	private final Camera					camera;
	private final LocalPlayerMoveHandler	playerController;
	private final PathFinder				pathFinder;
	private final IStateMachine				stateMachine;

	private ITarget							currentTarget;

	@Inject
	public SimpleAIPlayer(final IWorld world, final LocalGameState gameState, final Camera camera,
			final LocalPlayerMoveHandler playerController, final PathFinder pathFinder, final IStateMachine stateMachine) {

		Preconditions.checkNotNull( world );
		Preconditions.checkNotNull( gameState );
		Preconditions.checkNotNull( camera );
		Preconditions.checkNotNull( playerController );
		Preconditions.checkNotNull( pathFinder );
		Preconditions.checkNotNull( stateMachine );

		this.world = world;
		this.gameState = gameState;
		this.camera = camera;
		this.playerController = playerController;
		this.pathFinder = pathFinder;
		this.stateMachine = stateMachine;
	}

	@Override
	public void doNextStep() {
		if ( gameState.isGameInitialized() ) {

			if ( currentTarget == null || currentTarget.achieved() ) {
				currentTarget = findTarget();
				logger.info( "Nothing to do. Next target will be " + currentTarget );
			} else {
				ITarget possibleTarget = findTarget();
				if ( possibleTarget.getPriority() > currentTarget.getPriority()
						&& !(possibleTarget.equals( currentTarget )) ) {
					logger.info( "Discovered target with higher priority: " + possibleTarget );
					currentTarget = possibleTarget;
				}
			}

			currentTarget.approach();
		}
	}

	private ITarget findTarget() {
		TileCoordinate currentPos = world.findTile( gameState.getPlayer() );
		//
		// 1. Slay nearby enemy - if found
		//
		Actor enemy = findNearbyEntity( currentPos, Actor.class );
		if ( enemy != null ) {
			return new SlayEnemyTarget( enemy );
		}
		//
		// 2. Open nearby crate - if found
		//
		Crate crate = findNearbyEntity( currentPos, Crate.class );
		if ( crate != null && crate.getItem() != null ) {
			return new OpenCrateTarget( crate );
		}
		//
		// 3. Walk to closest crate - if any visible
		//
		crate = findNearestEntity( currentPos, Crate.class );
		if ( crate != null && crate.getItem() != null ) {
			return new OpenCrateTarget( crate );
		}
		//
		// 4. Walk to closest enemy - if any visible
		//
		enemy = findNearestEntity( currentPos, Actor.class );
		if ( enemy != null ) {
			return new SlayEnemyTarget( enemy );
		}
		//
		// 5. Walk to random position
		//
		return new WalkToPositionTarget( currentPos );
	}

	private <T extends Entity> T findNearestEntity( final TileCoordinate center, final Class<T> clazz ) {
		return null;
	}

	private <T extends Entity> T findNearbyEntity( final TileCoordinate center, final Class<T> clazz ) {
		T actor = getEntityOrNull( center.plus( 1, 1 ), clazz );
		if ( actor == null ) {
			actor = getEntityOrNull( center.plus( 1, 0 ), clazz );
		}
		if ( actor == null ) {
			actor = getEntityOrNull( center.plus( 1, -1 ), clazz );
		}
		if ( actor == null ) {
			actor = getEntityOrNull( center.plus( 0, -1 ), clazz );
		}
		if ( actor == null ) {
			actor = getEntityOrNull( center.plus( 0, 1 ), clazz );
		}
		if ( actor == null ) {
			actor = getEntityOrNull( center.plus( -1, 1 ), clazz );
		}
		if ( actor == null ) {
			actor = getEntityOrNull( center.plus( -1, 0 ), clazz );
		}
		if ( actor == null ) {
			actor = getEntityOrNull( center.plus( -1, -1 ), clazz );
		}
		return actor;
	}

	private <T extends Entity> T getEntityOrNull( final TileCoordinate coord, final Class<T> clazz ) {
		Tile tile = world.getTile( coord );
		Entity entity = tile.getEntityOnTile();
		if ( entity == null || !clazz.isInstance( entity ) ) {
			return null;
		}
		return clazz.cast( entity );
	}

	private void gotoTile( final TileCoordinate coord ) {
		Camera localCamera = camera.getReadOnly();

		float cameraCenterX = localCamera.getCenterX();
		float cameraCenterY = localCamera.getCenterY();

		int tileX = Camera.worldToTile( coord.getX() );
		int tileY = Camera.worldToTile( coord.getY() );
		int centerX = Camera.worldToTile( cameraCenterX );
		int centerY = Camera.worldToTile( cameraCenterY );

		int fromX = centerX - localCamera.getStartTileX();
		int fromY = centerY - localCamera.getStartTileY();
		int toX = tileX - localCamera.getStartTileX();
		int toY = tileY - localCamera.getStartTileY();

		Path path = pathFinder.findPath( gameState.getPlayer(), fromX, fromY, toX, toY );
		Path worldPath = new Path();
		if ( path != null ) {
			for ( int i = 0; i < path.getLength(); i++ ) {
				Step step = path.getStep( i );
				worldPath.appendStep( step.getX() + localCamera.getStartTileX(),
						step.getY() + localCamera.getStartTileY() );
			}
		}
		playerController.setPath( stateMachine, worldPath );
	}
}
