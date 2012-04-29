package org.blockout.ui;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.slick2d.NiftyBasicGameState;

@Named
public class MenuScreenGameState extends NiftyBasicGameState {

	private final Set<ScreenController>	screenControllers;

	@Inject
	public MenuScreenGameState(final Set<ScreenController> screenControllers) {
		this.screenControllers = screenControllers;
	}

	@Override
	protected void prepareNifty( final Nifty nifty, final StateBasedGame game ) {
		nifty.fromXml( "menuscreen.xml", "start", screenControllers.toArray( new ScreenController[0] ) );
	}

	@Override
	public int getID() {
		return 1;
	}

}
