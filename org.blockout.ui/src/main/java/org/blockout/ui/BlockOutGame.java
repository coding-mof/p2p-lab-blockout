package org.blockout.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Adapter class to implement our game into the Slick-GameLoop.
 * 
 * @author Marc-Christian Schulze
 * 
 */
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
		// We need to set the InGameGameState (with the lowest id) first
		// otherwise we might see for a second the in game screen on startup and
		// get some rendering issues when switching to in-game game state
		ArrayList<GameState> list = new ArrayList<GameState>( gameStates );
		Collections.sort( list, new Comparator<GameState>() {

			@Override
			public int compare( final GameState o1, final GameState o2 ) {
				if ( o1.getID() - o2.getID() < 0 ) {
					return -1;
				}
				if ( o1.getID() - o2.getID() > 0 ) {
					return 1;
				}
				return 0;
			}
		} );
		for ( GameState state : list ) {
			addState( state );
		}
		this.enterState( GameStates.StartMenu.ordinal() );
	}
}
