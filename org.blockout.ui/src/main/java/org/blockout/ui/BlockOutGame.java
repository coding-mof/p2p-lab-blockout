package org.blockout.ui;

import javax.inject.Inject;
import javax.inject.Named;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

@Named
public class BlockOutGame extends StateBasedGame {

	private final MenuScreenGameState	gameState;

	@Inject
	public BlockOutGame(final MenuScreenGameState gameState) {
		super( "BlockOut" );
		this.gameState = gameState;
	}

	@Override
	public void initStatesList( final GameContainer gc ) throws SlickException {

		addState( gameState );
	}

}
