package org.blockout.ui;

import javax.inject.Named;

import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;

@Named
public class StartScreenController implements StateBasedScreenController {

	private static final Logger	logger;
	static {
		logger = LoggerFactory.getLogger( StartScreenController.class );
	}

	private Nifty				nifty;

	public void startGame() {
		nifty.gotoScreen( "selectProfile" );
	}

	public void showSettings() {
		nifty.gotoScreen( "settings" );
	}

	public void exitGame() {
		logger.info( "exitGame" );
	}

	@Override
	public void bind( final Nifty paramNifty, final Screen paramScreen ) {
		nifty = paramNifty;
	}

	@Override
	public void onStartScreen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindToGameState( final StateBasedGame game, final GameState state ) {
		// TODO Auto-generated method stub

	}
}
