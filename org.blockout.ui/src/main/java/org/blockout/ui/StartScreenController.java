package org.blockout.ui;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.logic.PlayerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.lessvoid.nifty.screen.DefaultScreenController;

@Named
public class StartScreenController extends DefaultScreenController {

	private static final Logger	logger;
	static {
		logger = LoggerFactory.getLogger( StartScreenController.class );
	}

	@Inject
	public StartScreenController(final PlayerManager m) {

	}

	public void startGame() {
		gotoScreen( "selectProfile" );
	}

	public void showSettings() {
		logger.info( "showSettings" );
	}

	public void exitGame() {
		logger.info( "exitGame" );
	}
}
