package org.blockout.ui;

import javax.inject.Named;

import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;

/**
 * Screen controller implementation for the connection screen. On the connection
 * screen you can either directly connect to a peer by it's ip address or select
 * one from auto-discovery.
 * 
 * @author Marc-Christian Schulze
 * 
 */
@Named
public class ConnectionScreenController implements StateBasedScreenController {

	// Own state
	protected Nifty				nifty;
	protected StateBasedGame	game;

	@Override
	public void bind( final Nifty nifty, final Screen screen ) {
		this.nifty = nifty;
	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartScreen() {
		// TODO Auto-generated method stub

	}

	public void goBack() {
		nifty.gotoScreen( "selectProfile" );
	}

	public void connectToIp() {
		game.enterState( GameStates.InGame.ordinal(), new FadeOutTransition(), new FadeInTransition() );
	}

	@Override
	public void bindToGameState( final StateBasedGame game, final GameState state ) {
		this.game = game;
	}
}