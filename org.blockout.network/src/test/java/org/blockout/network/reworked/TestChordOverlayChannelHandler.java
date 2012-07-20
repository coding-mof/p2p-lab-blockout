package org.blockout.network.reworked;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import org.blockout.network.LocalNode;
import org.blockout.network.dht.IHash;
import org.blockout.network.dht.WrappedRange;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;

public class TestChordOverlayChannelHandler {

	private LocalNode					localNode;
	private ChordOverlayChannelHandler	chord;
	private Runnable					stabilization;

	@Before
	public void setUp() {
		localNode = new LocalNode();
		chord = new ChordOverlayChannelHandler( localNode, new TaskScheduler() {

			@Override
			public ScheduledFuture scheduleWithFixedDelay( final Runnable arg0, final Date arg1, final long arg2 ) {
				stabilization = arg0;
				return null;
			}

			@Override
			public ScheduledFuture scheduleWithFixedDelay( final Runnable arg0, final long arg1 ) {
				stabilization = arg0;
				return null;
			}

			@Override
			public ScheduledFuture scheduleAtFixedRate( final Runnable arg0, final Date arg1, final long arg2 ) {
				stabilization = arg0;
				return null;
			}

			@Override
			public ScheduledFuture scheduleAtFixedRate( final Runnable arg0, final long arg1 ) {
				stabilization = arg0;
				return null;
			}

			@Override
			public ScheduledFuture schedule( final Runnable arg0, final Date arg1 ) {
				stabilization = arg0;
				return null;
			}

			@Override
			public ScheduledFuture schedule( final Runnable arg0, final Trigger arg1 ) {
				stabilization = arg0;
				return null;
			}
		}, new SyncTaskExecutor(), 0 );
	}

	@Test
	public void testInitialResponsibility() {
		WrappedRange<IHash> responsibility = chord.getResponsibility();

		assertEquals( localNode.getNodeId(), responsibility.getUpperBound() );
		assertEquals( localNode.getNodeId().getNext(), responsibility.getLowerBound() );
	}

	@Test
	public void testInitialLinks() {
		assertEquals( localNode.getNodeId(), chord.getLocalId() );
		assertEquals( localNode.getNodeId(), chord.getSuccessor() );
		assertEquals( localNode.getNodeId(), chord.getPredecessor() );
	}

	@Test
	public void testInitialization() {
		IConnectionManager connectionMgr = Mockito.mock( IConnectionManager.class );
		chord.init( connectionMgr );
	}

	@Test
	public void testLoopbackRoutingUsingNodeId() {
		String message = "Any message";
		ChordListener listener = Mockito.mock( ChordListener.class );
		chord.addChordListener( listener );

		chord.sendMessage( message, localNode.getNodeId() );

		Mockito.verify( listener ).receivedMessage( chord, message, localNode.getNodeId() );
	}

	@Test
	public void testLoopbackRoutingUsingKey() {
		String message = "Any message";
		ChordListener listener = Mockito.mock( ChordListener.class );
		chord.addChordListener( listener );

		chord.sendMessage( message, localNode.getNodeId().getNext() );

		Mockito.verify( listener ).receivedMessage( chord, message, localNode.getNodeId() );
	}

}
