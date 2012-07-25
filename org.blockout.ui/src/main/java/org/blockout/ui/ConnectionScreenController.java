package org.blockout.ui;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.network.discovery.DiscoveryListener;
import org.blockout.network.discovery.INodeDiscovery;
import org.blockout.network.reworked.ConnectionManager;
import org.blockout.world.IWorld;
import org.blockout.world.LocalGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import com.google.common.base.Splitter;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.TextField;
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
	protected Nifty					nifty;
	protected Screen				screen;
	protected StateBasedGame		game;
	protected INodeDiscovery		discovery;
	protected IWorld				world;
	protected LocalGameState		gameState;
	private final ConnectionManager	connectionMgr;

	protected List<SocketAddress>	knownNodes;

	@Inject
	public ConnectionScreenController(final LocalGameState gameState, final IWorld world,
			final INodeDiscovery discovery, final ConnectionManager connectionMgr) {
		this.gameState = gameState;
		this.world = world;
		this.discovery = discovery;
		this.connectionMgr = connectionMgr;
		knownNodes = new ArrayList<SocketAddress>( discovery.listNodes() );
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

	public void connectToIP() {
		TextField field = screen.findNiftyControl( "txtIP", TextField.class );

		Iterable<String> split = Splitter.on( ":" ).split( field.getText() );
		Iterator<String> iter = split.iterator();
		try {
			String host = iter.next();
			String port = iter.next();
			connectionMgr.connectTo( new InetSocketAddress( InetAddress.getByName( host ), Integer.parseInt( port ) ) );
		} catch ( NumberFormatException e ) {
			e.printStackTrace();
		} catch ( UnknownHostException e ) {
			e.printStackTrace();
		} catch ( NoSuchElementException e ) {
			e.printStackTrace();
		}
	}

	public void connectToPeer() {
		try {
			world.init( gameState.getPlayer() );
			game.enterState( GameStates.InGame.ordinal(), new FadeOutTransition(), new FadeInTransition() );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	@Override
	public void bindToGameState( final StateBasedGame game, final GameState state ) {
		this.game = game;
	}

	@Override
	public void nodeDiscovered( final SocketAddress info ) {
		knownNodes.add( info );
		updateNodeList();
	}

	protected void updateNodeList() {
		@SuppressWarnings("unchecked")
		ListBox<SocketAddress> listBox = screen.findNiftyControl( "listPeers", ListBox.class );
		listBox.clear();

		listBox.addAllItems( new ArrayList<SocketAddress>( knownNodes ) );
	}
}
