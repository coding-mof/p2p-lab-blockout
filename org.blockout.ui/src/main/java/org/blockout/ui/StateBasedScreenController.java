package org.blockout.ui;

import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.screen.ScreenController;

/**
 * Common interface for all screen controllers that require access to the
 * {@link StateBasedGame} instance.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public interface StateBasedScreenController extends ScreenController {

	public void bindToGameState( StateBasedGame game, GameState state );
}
