package org.blockout.network.reworked;

import java.io.Serializable;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

import org.blockout.network.LocalNode;
import org.blockout.network.dht.IHash;
import org.blockout.network.dht.WrappedRange;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ChildChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

import com.google.common.base.Preconditions;

/**
 * Chord overlay for multiple {@link Channel}s of the netty framework.
 * 
 * TODO: Implement finger table to increase performance
 * 
 * @author Marc-Christian Schulze
 * 
 */
@ChannelHandler.Sharable
public class ChordOverlayChannelHandler extends SimpleChannelHandler implements IChordOverlay {

	private static final Logger				logger;
	static {
		logger = LoggerFactory.getLogger( ChordOverlayChannelHandler.class );
	}

	private final Set<FindSuccessorFuture>	pendingSuccessorLookups;
	private final List<ChordListener>		listener;
	private WrappedRange<IHash>				responsibility;

	private IHash							successorId;
	private Channel							successorChannel;
	private IHash							predeccessorId;
	private Channel							predeccessorChannel;

	private final ChannelGroup				channels;
	private final LocalNode					localNode;
	private final TaskExecutor				executor;
	private final IConnectionManager		connectionMgr;

	/**
	 * Creates a new chord overlay with the initial responsibility of
	 * <code>(local_node_id + 1, local_node_id]</code>.
	 * 
	 * @param localNode
	 *            The local node to retrieve the local node id from.
	 * @param executor
	 *            An executor for dispatching the listener invocations.
	 */
	public ChordOverlayChannelHandler(final LocalNode localNode, final TaskExecutor executor,
			final IConnectionManager connectionMgr) {

		Preconditions.checkNotNull( localNode );
		Preconditions.checkNotNull( executor );
		Preconditions.checkNotNull( connectionMgr );

		this.localNode = localNode;
		this.executor = executor;
		this.connectionMgr = connectionMgr;

		pendingSuccessorLookups = Collections.synchronizedSet( new HashSet<FindSuccessorFuture>() );
		listener = new CopyOnWriteArrayList<ChordListener>();
		channels = new DefaultChannelGroup();

		IHash ownNodeId = localNode.getNodeId();
		responsibility = new WrappedRange<IHash>( ownNodeId.getNext(), ownNodeId );
		logger.info( "Initialized chord with range = " + responsibility );
	}

	@Override
	public void messageReceived( final ChannelHandlerContext ctx, final MessageEvent e ) throws Exception {

		Object message = e.getMessage();
		if ( message instanceof FindSuccessorMessage ) {
			handleFindSuccessorMessage( e, (FindSuccessorMessage) message );
		} else if ( message instanceof SuccessorFoundMessage ) {
			handleFoundSuccessorMessage( (SuccessorFoundMessage) message );
		} else if ( message instanceof JoinMessage ) {
			handleJoinMessage( e, (JoinMessage) message );
		} else {
			// TODO: What is the origin's hash?
			fireMessageReceived( null, message );
		}

		super.messageReceived( ctx, e );
	}

	/**
	 * Invoked when a {@link FindSuccessorMessage} has been received. It
	 * responds with a {@link SuccessorFoundMessage} if we are the successor and
	 * forwards the message to our successor if not.
	 * 
	 * @param e
	 * @param msg
	 */
	private void handleFindSuccessorMessage( final MessageEvent e, final FindSuccessorMessage msg ) {

		WrappedRange<IHash> range = new WrappedRange<IHash>( localNode.getNodeId(), successorId );
		if ( range.contains( msg.getKey() ) ) {
			// I'm the successor
			logger.debug( "Local node is the successor of " + msg.getKey() );
			SuccessorFoundMessage responseMsg;
			responseMsg = new SuccessorFoundMessage( msg.getOrigin(), msg.getKey(), localNode.getNodeId() );
			Channels.write( e.getChannel(), responseMsg );
			return;
		}
		// Ask my successor
		if ( logger.isDebugEnabled() ) {
			String description = "Forwarding successor lookup to own successor {} ({})";
			logger.debug( description, successorChannel.getRemoteAddress(), successorId );
		}
		successorChannel.write( msg );
	}

	/**
	 * Invoked when a {@link SuccessorFoundMessage} has been received. It
	 * completes all pending successor lookups with the same key and forwards
	 * all messages not destined for this node to our predecessor.
	 * 
	 * @param ctx
	 * @param e
	 * @param msg
	 */
	private void handleFoundSuccessorMessage( final SuccessorFoundMessage msg ) {

		// We don't care if this is a response to our lookup or someone else's
		completeFutures( msg );

		if ( !msg.getDestination().equals( localNode.getNodeId() ) ) {
			// We need to route the message back to our predecessor
			Channels.write( predeccessorChannel, msg );
		}
	}

	private void handleJoinMessage( final MessageEvent e, final JoinMessage message ) {

		if ( responsibility.contains( message.getNodeId() ) ) {
			// We are the new node's successor
			welcomeNode( e, message );
			return;
		}

		// Route message to successor of new node
		ObservableFuture<IHash> future = findSuccessor( message.getNodeId() );
		future.addFutureListener( new FutureListener<IHash>() {

			@Override
			public void completed( final ObservableFuture<IHash> future ) {
				try {
					IHash nodeId = future.get();
					logger.debug( "Found successor " + nodeId + " of new node " + message.getNodeId() );
					routeMessage( message, nodeId );
				} catch ( InterruptedException e ) {
					logger.error( "Someone interrupted the successor lookup.", e );
				} catch ( ExecutionException e ) {
					logger.error( "Successor lookup failed.", e );
				}
			}
		} );
	}

	private void welcomeNode( final MessageEvent e, final JoinMessage message ) {
		ConnectionFuture future = connectionMgr.connectTo( message.getAddress() );
		future.addListener( new ConnectionFutureListener() {

			@Override
			public void operationComplete( final ConnectionFuture connectFuture ) throws Exception {
				if ( !connectFuture.isSuccess() ) {
					logger.error( "Couldn't connect to new node " + message.getAddress(), connectFuture.getCause() );
					return;
				}

				Channel channel = findChannelConnectedTo( message.getAddress() );
				if ( channel == null ) {
					// How can that happen?
					// Did the connection crash in the meantime?
					logger.error( "Connection to {} seems to be crashed in the meantime", message.getAddress() );
					return;
				}

				// Send welcome message
				WelcomeMessage welcomeMsg;
				SocketAddress serverAddress = connectionMgr.getServerAddress();
				SocketAddress remoteAddress = predeccessorChannel.getRemoteAddress();
				welcomeMsg = new WelcomeMessage( localNode.getNodeId(), serverAddress, predeccessorId, remoteAddress );
				Channels.write( channel, welcomeMsg );

				// Update our predecessor reference
				predeccessorId = message.getNodeId();
				predeccessorChannel = e.getChannel();

				// Update responsibility
				WrappedRange<IHash> newResponsibility;
				newResponsibility = new WrappedRange<IHash>( predeccessorId.getNext(), localNode.getNodeId() );
				fireResponsibilityChanged( responsibility, newResponsibility );
				responsibility = newResponsibility;
			}
		} );
	}

	private Channel findChannelConnectedTo( final SocketAddress address ) {
		for ( Channel channel : channels ) {
			if ( channel.getRemoteAddress().equals( address ) ) {
				return channel;
			}
		}
		return null;
	}

	/**
	 * Completes all pending successor lookup futures that wait for the key of
	 * the given message.
	 * 
	 * @param msg
	 *            The message about the discovered successor
	 */
	private void completeFutures( final SuccessorFoundMessage msg ) {
		synchronized ( pendingSuccessorLookups ) {
			for ( FindSuccessorFuture future : pendingSuccessorLookups ) {
				if ( future.getKey().equals( msg.getKey() ) ) {
					future.complete( msg.getSuccessor() );
					// Don't abort since there might be multiple pending lookups
				}
			}
		}
	}

	@Override
	public void channelOpen( final ChannelHandlerContext ctx, final ChannelStateEvent e ) throws Exception {
		channels.add( e.getChannel() );
		// We connected as client - so we need to join the existing ring
		Channels.write( e.getChannel(), new JoinMessage( localNode.getNodeId(), connectionMgr.getServerAddress() ) );
		super.channelOpen( ctx, e );
	}

	@Override
	public void childChannelOpen( final ChannelHandlerContext ctx, final ChildChannelStateEvent e ) throws Exception {
		// Someone connected to us
		channels.add( e.getChannel() );
		super.childChannelOpen( ctx, e );
	}

	@Override
	public WrappedRange<IHash> getResponsibility() {
		return responsibility;
	}

	@Override
	public ObservableFuture<IHash> findSuccessor( final IHash key ) {
		FindSuccessorFuture future = findPendingSuccessorLookup( key );
		if ( future != null ) {
			// There is already a pending lookup
			// for the successor of the same key
			return future;
		}
		future = new FindSuccessorFuture( pendingSuccessorLookups, key );
		if ( responsibility.contains( key ) ) {
			// Local node is the successor
			future.complete( localNode.getNodeId() );
			return future;
		}
		// Send a lookup message to our successor
		Channels.write( successorChannel, new FindSuccessorMessage( localNode.getNodeId(), key ) );
		return future;
	}

	/**
	 * Returns the first {@link FindSuccessorFuture} already pending for the
	 * given key if present; otherwise null.
	 * 
	 * @param key
	 * @return The pending future or null if no present.
	 */
	private FindSuccessorFuture findPendingSuccessorLookup( final IHash key ) {
		synchronized ( pendingSuccessorLookups ) {
			for ( FindSuccessorFuture future : pendingSuccessorLookups ) {
				if ( future.getKey().equals( key ) ) {
					return future;
				}
			}
		}
		return null;
	}

	@Override
	public void addChordListener( final ChordListener l ) {
		listener.add( l );
	}

	@Override
	public void removeChordListener( final ChordListener l ) {
		listener.remove( l );
	}

	/**
	 * Notifies all listener about the responsibility changed using the
	 * {@link TaskExecutor} passed in the constructor for dispatching.
	 * 
	 * @param from
	 * @param to
	 */
	private void fireResponsibilityChanged( final WrappedRange<IHash> from, final WrappedRange<IHash> to ) {
		logger.info( "Responsibility changed from " + from + " to " + to );
		for ( final ChordListener l : listener ) {
			executor.execute( new Runnable() {

				@Override
				public void run() {
					l.responsibilityChanged( ChordOverlayChannelHandler.this, from, to );
				}
			} );
		}
	}

	private void fireMessageReceived( final IHash from, final Object message ) {
		logger.info( "Received message " + message + " from " + from );
		for ( final ChordListener l : listener ) {
			executor.execute( new Runnable() {

				@Override
				public void run() {
					l.receivedMessage( ChordOverlayChannelHandler.this, message, from );
				}
			} );
		}
	}

	@Override
	public void routeMessage( final Serializable message, final IHash nodeId ) {
		logger.debug( "Routing message " + message + " to " + nodeId );
		// TODO: Implement routing
	}
}
