package org.blockout.network.reworked;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.blockout.network.LocalNode;
import org.blockout.network.dht.IHash;
import org.blockout.network.dht.WrappedRange;
import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

import com.google.common.base.Preconditions;

public abstract class AbstractChordHandler extends ChannelInterceptorAdapter implements IChordOverlay {
	private static final Logger				logger;
	static {
		logger = LoggerFactory.getLogger( ChordOverlayChannelHandler.class );
	}

	private final LocalNode					localNode;

	private IHash							successorId;
	private Channel							successorChannel;

	private IHash							predecessorId;
	private Channel							predecessorChannel;

	private final List<ChordListener>		listener;
	private volatile WrappedRange<IHash>	responsibility;

	private final TaskExecutor				executor;
	private final LookupTable				lookupTable;

	public AbstractChordHandler(final LocalNode localNode, final TaskExecutor executor) {

		Preconditions.checkNotNull( localNode );

		this.localNode = localNode;
		this.executor = executor;
		listener = new CopyOnWriteArrayList<ChordListener>();
		lookupTable = new LookupTable();

		successorId = localNode.getNodeId();
		predecessorId = localNode.getNodeId();

		IHash ownNodeId = localNode.getNodeId();
		responsibility = new WrappedRange<IHash>( ownNodeId.getNext(), ownNodeId );
		logger.info( "Initialized chord with range = " + responsibility );
	}

	public LookupTable getLookupTable() {
		return lookupTable;
	}

	@Override
	public WrappedRange<IHash> getResponsibility() {
		return responsibility;
	}

	public IHash getSuccessorId() {
		return successorId;
	}

	public Channel getSuccessorChannel() {
		return successorChannel;
	}

	public IHash getPredecessorId() {
		return predecessorId;
	}

	public Channel getPredecessorChannel() {
		return predecessorChannel;
	}

	@Override
	public IHash getPredecessor() {
		return predecessorId;
	}

	@Override
	public IHash getSuccessor() {
		return successorId;
	}

	@Override
	public IHash getLocalId() {
		return responsibility.getUpperBound();
	}

	@Override
	public void addChordListener( final ChordListener l ) {
		listener.add( l );
	}

	@Override
	public void removeChordListener( final ChordListener l ) {
		listener.remove( l );
	}

	protected void updateResponsibility( final IHash lowerBound ) {
		WrappedRange<IHash> newResponsibility;
		newResponsibility = new WrappedRange<IHash>( lowerBound, localNode.getNodeId() );
		WrappedRange<IHash> tmp = responsibility;
		if ( !newResponsibility.equals( tmp ) ) {
			logger.info( "Responsibility changed from " + tmp + " to " + newResponsibility );
			responsibility = newResponsibility;
			fireResponsibilityChanged( tmp, newResponsibility );
		}
	}

	protected void changePredecessor( final IHash newPredecessor, final Channel newChannel ) {
		if ( !predecessorId.equals( newPredecessor ) ) {
			predecessorId = newPredecessor;
			predecessorChannel = newChannel;
			if ( predecessorChannel != null && predecessorChannel.isConnected() ) {
				lookupTable.put( predecessorId, predecessorChannel );
			}
			firePredecessorChanged( predecessorId );
			updateResponsibility( predecessorId.getNext() );
		}
	}

	protected void changeSuccessor( final IHash newSuccessor, final Channel newChannel ) {
		if ( !successorId.equals( newSuccessor ) ) {

			// the new successor is only valid if when have no other node in our
			// routing table that is in the range between
			// [localNode+1, successor-1]
			IHash lowerBound = localNode.getNodeId().getNext();
			WrappedRange<IHash> range = new WrappedRange<IHash>( lowerBound, newSuccessor.getPrevious() );
			// synchronized ( lookupTable ) {
			//
			// if ( lookupTable.containsAnyOf( range ) ) {
			// logger.debug( "New successor " + newSuccessor +
			// " is not better than current " + successorId + "." );
			// return;
			// }
			// }

			successorId = newSuccessor;
			successorChannel = newChannel;
			fireSuccessorChanged( successorId );
		}
	}

	/**
	 * Notifies all listener about the responsibility changed using the
	 * {@link TaskExecutor} passed in the constructor for dispatching.
	 * 
	 * @param from
	 * @param to
	 */
	protected void fireResponsibilityChanged( final WrappedRange<IHash> from, final WrappedRange<IHash> to ) {
		for ( final ChordListener l : listener ) {
			executor.execute( new Runnable() {

				@Override
				public void run() {
					l.responsibilityChanged( AbstractChordHandler.this, from, to );
				}
			} );
		}
	}

	protected void fireSuccessorChanged( final IHash successor ) {
		for ( final ChordListener l : listener ) {
			executor.execute( new Runnable() {

				@Override
				public void run() {
					l.successorChanged( AbstractChordHandler.this, successor );
				}
			} );
		}
	}

	protected void firePredecessorChanged( final IHash predecessor ) {
		for ( final ChordListener l : listener ) {
			executor.execute( new Runnable() {

				@Override
				public void run() {
					l.predecessorChanged( AbstractChordHandler.this, predecessor );
				}
			} );
		}
	}

	protected void fireMessageReceived( final IHash from, final Object message ) {
		logger.info( "Received message " + message + " from " + from );
		for ( final ChordListener l : listener ) {
			executor.execute( new Runnable() {

				@Override
				public void run() {
					logger.debug( "Passing message " + message + " to " + l );
					l.receivedMessage( AbstractChordHandler.this, message, from );
				}
			} );
		}
	}
}
