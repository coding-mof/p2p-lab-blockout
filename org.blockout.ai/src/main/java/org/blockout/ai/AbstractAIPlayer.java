package org.blockout.ai;

import org.blockout.engine.Camera;
import org.blockout.ui.LocalPlayerMoveHandler;
import org.blockout.world.IWorld;
import org.blockout.world.LocalGameState;
import org.blockout.world.state.IStateMachine;
import org.newdawn.slick.util.pathfinding.PathFinder;

import com.google.common.base.Preconditions;

/**
 * Abstract base class for the artificial intelligence (AI)
 * 
 * @author Florian Müller
 */
public abstract class AbstractAIPlayer implements AIPlayer, AIContext {
    private final IWorld                 world;
    private final LocalGameState         gameState;
    private final Camera                 camera;
    private final LocalPlayerMoveHandler playerController;
    private final PathFinder             pathFinder;
    private final IStateMachine          stateMachine;

    /**
     * Constructs a new AIPlayer
     * 
     * @param world
     *            The world used for obtaining tiles.
     * @param gameState
     *            The current gamestate
     * @param camera
     *            The camera
     * @param playerController
     *            The controller to control the current player with the AI
     * @param pathFinder
     *            A pathfinder to find ways inside the world
     * @param stateMachine
     *            The statemachine of the game
     * 
     * @throws NullPointerException
     *             If you pass a null argument
     */
    public AbstractAIPlayer( final IWorld world,
            final LocalGameState gameState, final Camera camera,
            final LocalPlayerMoveHandler playerController,
            final PathFinder pathFinder, final IStateMachine stateMachine ) {

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
    public abstract void doNextStep();

    @Override
    public LocalGameState getGameState() {
        return gameState;
    }

    @Override
    public Camera getCamera() {
        return camera;
    }

    @Override
    public LocalPlayerMoveHandler getPlayerController() {
        return playerController;
    }

    @Override
    public IWorld getWorld() {
        return world;
    }

    @Override
    public IStateMachine getStateMachine() {
        return stateMachine;
    }

    @Override
    public PathFinder getPathfinder() {
        return pathFinder;
    }
}
