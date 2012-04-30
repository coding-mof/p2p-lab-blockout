package org.blockout.ui;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class BlockOutGame extends StateBasedGame {

	private final Set<GameState>	gameStates;

	public BlockOutGame() {
		super( "BlockOut" );
		gameStates = new HashSet<GameState>();
	}

	@Inject
	public void addGameStates( final Set<GameState> gameStates ) {
		this.gameStates.addAll( gameStates );
	}

	@Override
	public void initStatesList( final GameContainer gc ) throws SlickException {

		for ( GameState state : gameStates ) {
			System.out.println( "State: " + state );
			addState( state );
		}

		this.enterState( 1 );
	}

}
