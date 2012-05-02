package org.blockout.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.logic.PlayerManager;
import org.blockout.world.LocalGameState;
import org.blockout.world.Player;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;

@Named
public class ProfileScreenController implements StateBasedScreenController {

	private final LocalGameState	gameState;
	private final PlayerManager		playerManager;
	private Screen					screen;
	private Nifty					nifty;
	private Player					currentSelectedPlayer;
	private StateBasedGame			game;

	@Inject
	public ProfileScreenController(final PlayerManager playerManager, final LocalGameState gameState) {
		this.playerManager = playerManager;
		this.gameState = gameState;
	}

	@Override
	public void bind( final Nifty nifty, final Screen screen ) {

		this.nifty = nifty;
		this.screen = screen;

	}

	@Override
	public void onStartScreen() {
		fillProfilesListBox();
	}

	public void fillProfilesListBox() {
		@SuppressWarnings("unchecked")
		ListBox<String> listBox = screen.findNiftyControl( "playerProfiles", ListBox.class );
		listBox.clear();

		Set<String> players = playerManager.listPlayers();
		listBox.addAllItems( new ArrayList<String>( players ) );
	}

	@NiftyEventSubscriber(id = "playerProfiles")
	public void onMyListBoxSelectionChanged( final String id, final ListBoxSelectionChangedEvent<String> event ) {
		List<String> selection = event.getSelection();

		Label lblName = screen.findNiftyControl( "lblName", Label.class );
		Label lblLevel = screen.findNiftyControl( "lblLevel", Label.class );
		Label lblXP = screen.findNiftyControl( "lblXP", Label.class );
		Label lblHealth = screen.findNiftyControl( "lblHealth", Label.class );

		Button btnStartWithSelected = screen.findNiftyControl( "btnStartWithSelected", Button.class );
		btnStartWithSelected.setEnabled( selection.size() > 0 );

		if ( selection.size() == 0 ) {
			lblName.setText( "" );
			lblLevel.setText( "" );
			lblXP.setText( "" );
			lblHealth.setText( "" );
			currentSelectedPlayer = null;
			return;
		}

		String playerName = selection.get( 0 );
		try {
			currentSelectedPlayer = playerManager.loadPlayer( playerName );

			lblName.setText( playerName );
			lblLevel.setText( "" + currentSelectedPlayer.getLevel() );
			lblXP.setText( "" + currentSelectedPlayer.getExperience() );
			lblHealth.setText( "" + currentSelectedPlayer.getCurrentHealth() );

		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void goBack() {
		nifty.gotoScreen( "start" );
	}

	public void createNewPlayer() {
		TextField txtPlayerName = screen.findNiftyControl( "txtPlayerName", TextField.class );

		Player p = new Player( txtPlayerName.getText() );
		try {
			playerManager.savePlayer( p );
			startGame( p );
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startWithSelected() {
		startGame( currentSelectedPlayer );
	}

	private void startGame( final Player p ) {
		System.out.println( "Starting game with: " + p.getName() );
		gameState.setPlayer( p );
		game.enterState( 2, new FadeOutTransition(), new FadeInTransition() );
	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindToGameState( final StateBasedGame game, final GameState state ) {
		this.game = game;
	}
}
