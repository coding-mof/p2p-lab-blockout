package org.blockout.ai;

import javax.inject.Inject;

import org.blockout.common.TileCoordinate;
import org.blockout.ui.Camera;
import org.blockout.ui.LocalPlayerMoveHandler;
import org.blockout.world.IWorld;
import org.blockout.world.LocalGameState;
import org.blockout.world.entity.Player;
import org.blockout.world.state.IStateMachine;
import org.newdawn.slick.util.pathfinding.PathFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of a simple artificial intelligence (AI). It searches the
 * terrain for enemies and crates.
 * 
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class SimpleAIPlayer extends AbstractAIPlayer {

	private static final Logger				logger;
	static {
		logger = LoggerFactory.getLogger( SimpleAIPlayer.class );
	}

	@Inject
    public SimpleAIPlayer( final IWorld world, final LocalGameState gameState,
            final Camera camera, final LocalPlayerMoveHandler playerController,
            final PathFinder pathFinder, final IStateMachine stateMachine ) {

        super( world, gameState, camera, playerController, pathFinder,
                stateMachine );
	}

	@Override
	public void doNextStep() {
        if( getGameState().isGameInitialized() ) {

            Player p = getGameState().getPlayer();
            TileCoordinate currentPos = getWorld().findTile( p );
			logger.info( "I'm currently at " + currentPos );
			logger.info( "AI Player does next step as player: " + p );
            AIUtils.gotoTile( this,
                    currentPos.plus( new TileCoordinate( 10, 4 ) ) );
		}
	}
}
