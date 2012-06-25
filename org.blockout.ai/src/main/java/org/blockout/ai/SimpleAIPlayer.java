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
import org.newdawn.slick.util.pathfinding.PathFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of a simple artificial intelligence (AI). It searches the
 * terrain for enemies and crates.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class SimpleAIPlayer extends AbstractAIPlayer {

	private static final Logger	logger;
	static {
		logger = LoggerFactory.getLogger( SimpleAIPlayer.class );
	}

	private ITarget				currentTarget;

	@Inject
	public SimpleAIPlayer(final IWorld world, final LocalGameState gameState, final Camera camera,
			final LocalPlayerMoveHandler playerController, final PathFinder pathFinder, final IStateMachine stateMachine) {

		super( world, gameState, camera, playerController, pathFinder, stateMachine );
	}

	@Override
	public void doNextStep() {
		if ( getGameState().isGameInitialized() ) {

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

			logger.debug( "Current target: " + currentTarget );
			currentTarget.approach();
		}
	}

	private ITarget findTarget() {
		TileCoordinate currentPos = getWorld().findTile( getGameState().getPlayer() );
		//
		// 1. Slay nearby enemy - if found
		//
		Actor enemy = findNearbyEntity( currentPos, Actor.class );
		if ( enemy != null ) {
			return new SlayEnemyTarget( enemy, this );
		}
		//
		// 2. Open nearby crate - if found
		//
		Crate crate = findNearbyEntity( currentPos, Crate.class );
		if ( crate != null && crate.getItem() != null ) {
			return new OpenCrateTarget( crate, this );
		}
		//
		// 3. Walk to closest crate - if any visible
		//
		crate = findNearestEntity( currentPos, Crate.class );
		if ( crate != null && crate.getItem() != null ) {
			TileCoordinate tile = getWorld().findTile( crate );
			TileCoordinate coordinate = AIUtils.findWalkableTileNextTo( this, tile );
			if ( coordinate != null ) {
				return new WalkToPositionTarget( coordinate, this );
			} else {
				// Crate is unreachable
			}
		}
		//
		// 4. Walk to closest enemy - if any visible
		//
		enemy = findNearestEntity( currentPos, Actor.class );
		if ( enemy != null ) {
			TileCoordinate tile = getWorld().findTile( enemy );
			TileCoordinate coordinate = AIUtils.findWalkableTileNextTo( this, tile );
			if ( coordinate != null ) {
				return new WalkToPositionTarget( coordinate, this );
			} else {
				// Enemy is unreachable
			}
		}
		//
		// 5. Walk to random position
		//
		return new WalkToPositionTarget( currentPos, this );
	}

	private <T extends Entity> T findNearestEntity( final TileCoordinate center, final Class<T> clazz ) {
		// locks the camera state
		Camera localCamera = getCamera().getReadOnly();
		double distance = Double.MAX_VALUE;
		T result = null;

		for ( int y = 0; y < localCamera.getNumVerTiles(); y++ ) {
			for ( int x = 0; x < localCamera.getNumHorTiles(); x++ ) {
				TileCoordinate currentTile = center.plus( x, y );
				T entity = getEntityOrNull( currentTile, clazz );
				if ( entity != null ) {
					double dist = TileCoordinate.computeSquaredEuclidianDistance( center, currentTile );
					if ( dist < distance ) {
						distance = dist;
						result = entity;
					}
				}
			}
		}
		return result;
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
		Tile tile = getWorld().getTile( coord );
		Entity entity = tile.getEntityOnTile();
		if ( entity == null || !clazz.isInstance( entity ) ) {
			return null;
		}
		return clazz.cast( entity );
	}
}
