package org.blockout.network.reworked;

import java.io.Serializable;

import org.blockout.network.LocalNode;
import org.blockout.network.dht.IHash;
import org.blockout.network.dht.WrappedRange;
import org.jboss.netty.util.HashedWheelTimer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

public class ChordIntegrationTest {

	private LocalNode					localNode;
	private ChordOverlayChannelHandler	chord;
	private ConnectionManager			connectionMgr;

	private ThreadPoolTaskScheduler createPooledScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize( 10 );
		scheduler.initialize();
		return scheduler;
	}

	private IChordOverlay createAndConnectPeer( final LocalNode node, final ChordListener listener ) {
		ThreadPoolTaskScheduler scheduler = createPooledScheduler();
		ChordOverlayChannelHandler overlay;
		overlay = new ChordOverlayChannelHandler( node, scheduler, new SyncTaskExecutor(), 30000, 1000 );
		overlay.addChordListener( listener );
		ConnectionManager mgr;
		mgr = new ConnectionManager( new HashedWheelTimer(), new SyncTaskExecutor(), 3000, 6000, overlay );
		mgr.init();
		mgr.connectTo( connectionMgr.getServerAddress() );
		return overlay;
	}

	@Before
	public void setUp() {
		localNode = new LocalNode();
		ThreadPoolTaskScheduler scheduler = createPooledScheduler();
		chord = new ChordOverlayChannelHandler( localNode, scheduler, new SyncTaskExecutor(), 30000, 1000 );
		connectionMgr = new ConnectionManager( new HashedWheelTimer(), new SyncTaskExecutor(), 3000, 6000, chord );
		connectionMgr.init();
	}

	/**
	 * Tests that after connecting two peers, both responsibilities have changed
	 * accordingly.
	 */
	@Test
	public void testResponsibilityChanged() {
		ChordListener ownListener = Mockito.mock( ChordListener.class );
		chord.addChordListener( ownListener );
		WrappedRange<IHash> responsibility = chord.getResponsibility();

		LocalNode peerNode = new LocalNode();
		ChordListener peerListener = Mockito.mock( ChordListener.class );
		IChordOverlay peerChord = createAndConnectPeer( peerNode, peerListener );

		//
		// Verify that our responsibility has changed
		//
		Mockito.verify( ownListener, Mockito.timeout( 1000 ) ).responsibilityChanged( chord, responsibility,
				new WrappedRange<IHash>( peerNode.getNodeId().getNext(), localNode.getNodeId() ) );
		//
		// Verify that the peer's responsibility has changed
		//
		Mockito.verify( peerListener, Mockito.timeout( 1000 ) ).responsibilityChanged( peerChord,
				new WrappedRange<IHash>( peerNode.getNodeId().getNext(), peerNode.getNodeId() ),
				new WrappedRange<IHash>( localNode.getNodeId().getNext(), peerNode.getNodeId() ) );
	}

	@Test
	public void testMessageRouting() {
		ChordListener ownListener = Mockito.mock( ChordListener.class );
		chord.addChordListener( ownListener );
		WrappedRange<IHash> responsibility = chord.getResponsibility();

		LocalNode peerNode = new LocalNode();
		ChordListener peerListener = Mockito.mock( ChordListener.class );
		IChordOverlay peerChord = createAndConnectPeer( peerNode, peerListener );

		//
		// Verify that our responsibility has changed
		//
		Mockito.verify( ownListener, Mockito.timeout( 1000 ) ).responsibilityChanged( chord, responsibility,
				new WrappedRange<IHash>( peerNode.getNodeId().getNext(), localNode.getNodeId() ) );

		// Now that we are properly connected we can send our message
		String message = "Routed message";
		chord.sendMessage( message, peerNode.getNodeId() );

		//
		// Verify that the message is received by our peer
		//
		Mockito.verify( peerListener, Mockito.timeout( 2000 ) ).receivedMessage( peerChord, message,
				localNode.getNodeId() );
		Mockito.verify( ownListener, Mockito.never() ).receivedMessage( Mockito.any( IChordOverlay.class ),
				Mockito.any( Serializable.class ), Mockito.any( IHash.class ) );
	}
}
