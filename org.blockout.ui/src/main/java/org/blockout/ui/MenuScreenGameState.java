package org.blockout.ui;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.slick2d.NiftyBasicGameState;

/**
 * Starting game state which manages all menus.
 * 
 * @author Marc-Christian Schulze
 * 
 */
@Named
public class MenuScreenGameState extends NiftyBasicGameState {

	private final Set<StateBasedScreenController>	screenControllers;

	@Inject
	public MenuScreenGameState(final Set<StateBasedScreenController> screenControllers) {
		this.screenControllers = screenControllers;
	}

	@Override
	protected void prepareNifty( final Nifty nifty, final StateBasedGame game ) {
		ScreenController[] controller = new ScreenController[screenControllers.size()];
		int i = 0;
		for ( StateBasedScreenController c : screenControllers ) {
			c.bindToGameState( game, this );
			controller[i++] = c;
		}
		nifty.fromXml( "menuscreen.xml", "start", controller );
	}

	@Override
	public int getID() {
		return GameStates.StartMenu.ordinal();
	}

}
