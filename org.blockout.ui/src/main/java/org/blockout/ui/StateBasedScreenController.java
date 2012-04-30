package org.blockout.ui;

import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.screen.ScreenController;

public interface StateBasedScreenController extends ScreenController {

	public void bindToGameState( StateBasedGame game, GameState state );
}
