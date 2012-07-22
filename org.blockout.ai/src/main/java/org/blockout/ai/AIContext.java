package org.blockout.ai;

import org.blockout.engine.Camera;
import org.blockout.ui.LocalPlayerMoveHandler;
import org.blockout.world.IWorld;
import org.blockout.world.LocalGameState;
import org.blockout.world.state.IStateMachine;
import org.newdawn.slick.util.pathfinding.PathFinder;

/**
 * Implementation of this class provide the current game-context for the AI to
 * operate properly.
 * 
 * @author Florian Müller
 */
public interface AIContext {

    /**
     * Returns the current wolrd of the game
     * 
     * @return World of the game
     */
    public IWorld getWorld();

    /**
     * Returns the current game-state
     * 
     * @return Current gamestate
     */
    public LocalGameState getGameState();

    /**
     * Returns the controller for the player which is controlled by the AI
     * 
     * @return Controller for the player
     */
    public LocalPlayerMoveHandler getPlayerController();

    /**
     * Returns the Camera which is used for the controlled player
     * 
     * @return The Camera of the controlled player
     */
    public Camera getCamera();

    /**
     * Returns the statemachine of the game
     * 
     * @return Statemachine of the game
     */
    public IStateMachine getStateMachine();

    /**
     * Returns a pathfinder for the world
     * 
     * @return Pathfinder for the world
     */
    public PathFinder getPathfinder();
}
