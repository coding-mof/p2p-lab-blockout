package org.blockout.ai;

import javax.inject.Inject;

import org.blockout.common.TileCoordinate;
import org.blockout.ui.Camera;
import org.blockout.ui.LocalPlayerMoveHandler;
import org.blockout.world.IWorld;
import org.blockout.world.LocalGameState;
import org.blockout.world.entity.Player;
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

			Player p = gameState.getPlayer();
			TileCoordinate currentPos = world.findTile( p );
			logger.info( "I'm currently at " + currentPos );
			logger.info( "AI Player does next step as player: " + p );
			gotoTile( currentPos.plus( new TileCoordinate( 10, 4 ) ) );
		}
	}

	private void gotoTile( final TileCoordinate coord ) {
		Camera localCamera = camera.getReadOnly();

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
