package org.blockout.ui;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.network.INodeAddress;
import org.blockout.network.NodeInfo;
import org.blockout.network.discovery.DiscoveryListener;
import org.blockout.network.discovery.INodeDiscovery;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ListBox;
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
public class ConnectionScreenController implements StateBasedScreenController, DiscoveryListener {

	// Own state
	protected Nifty				nifty;
	protected Screen			screen;
	protected StateBasedGame	game;
	protected INodeDiscovery	discovery;
	protected List<INodeAddress> knownNodes;

	@Inject
	public ConnectionScreenController(final INodeDiscovery discovery) {
		this.discovery = discovery;
		knownNodes = new ArrayList<INodeAddress>(discovery.listNodes());
	}

	@Override
	public void bind( final Nifty nifty, final Screen screen ) {
		this.nifty = nifty;
		this.screen = screen;
	}

	@Override
	public void onEndScreen() {
		discovery.removeDiscoveryListener( this );
	}

	@Override
	public void onStartScreen() {
		knownNodes.clear();
		knownNodes.addAll( discovery.listNodes() );
		discovery.addDiscoveryListener( this );
		updateNodeList();
	}

	public void goBack() {
		nifty.gotoScreen( "selectProfile" );
	}

	public void connectToPeer() {
		game.enterState( GameStates.InGame.ordinal(), new FadeOutTransition(), new FadeInTransition() );
	}

	@Override
	public void bindToGameState( final StateBasedGame game, final GameState state ) {
		this.game = game;
	}

	@Override
	public void nodeDiscovered( final NodeInfo info ) {
		knownNodes.add( info );
		updateNodeList();
	}

	protected void updateNodeList() {
		@SuppressWarnings("unchecked")
		ListBox<INodeAddress> listBox = screen.findNiftyControl("listPeers",
				ListBox.class);
		listBox.clear();

		listBox.addAllItems(new ArrayList<INodeAddress>(knownNodes));
	}
}
