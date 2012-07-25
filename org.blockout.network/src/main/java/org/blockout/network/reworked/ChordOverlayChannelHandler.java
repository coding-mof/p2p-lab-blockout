package org.blockout.network.reworked;

import java.io.Serializable;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.blockout.network.LocalNode;
import org.blockout.network.dht.IHash;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;

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
public class ChordOverlayChannelHandler extends AbstractChordHandler {

	private static final Logger				logger;
	static {
		logger = LoggerFactory.getLogger( ChordOverlayChannelHandler.class );
	}

	private final Set<FindSuccessorFuture>	pendingSuccessorLookups;

	private final ChannelGroup				channels;
	private final LocalNode					localNode;
	private final TaskScheduler				scheduler;
	/**
	 * Contains the {@link SocketAddress}' of peers that we should not send a
	 * {@link JoinRequestMessage}. The node that welcomes a new peer in the ring
	 * stores the peer's server address in this set, so that when we have
	 * connected to the new peer as client - we don't send a
	 * {@link JoinRequestMessage} again.
	 */
	private final Set<SocketAddress>		joinRequestFilter;

	private IConnectionManager				connectionMgr;

	private final long						stabilizationRate;
	private final long						notificationRate;
	private final long						propagationDelay;

	/**
	 * Creates a new chord overlay with the initial responsibility of
	 * <code>(local_node_id + 1, local_node_id]</code>.
	 * 
	 * @param localNode
	 *            The local node to retrieve the local node id from.
	 * @param scheduler
	 *            An executor for dispatching the listener invocations.
	 */
	public ChordOverlayChannelHandler(final LocalNode localNode, final TaskScheduler scheduler,
			final TaskExecutor executor, final long stabilizationRate, final long notificationRate,
			final long propagationDelay) {

		super( localNode, executor );

		Preconditions.checkNotNull( localNode );
		Preconditions.checkNotNull( scheduler );
		Preconditions.checkNotNull( executor );

		this.localNode = localNode;
		this.scheduler = scheduler;
		this.stabilizationRate = stabilizationRate;
		this.notificationRate = notificationRate;
		this.propagationDelay = propagationDelay;

		joinRequestFilter = Collections.synchronizedSet( new HashSet<SocketAddress>() );
		pendingSuccessorLookups = Collections.synchronizedSet( new HashSet<FindSuccessorFuture>() );
		channels = new DefaultChannelGroup();

		addChordListener( new ChordListenerAdapter() {

			@Override
			public void successorChanged( final IChordOverlay chord, final IHash successor ) {
				// Notify the new successor about us - so that
				// he can adjust his responsibility
				notifySuccessorAboutUs();
			}
		} );
	}

	@Override
	public void channelConnected( final IConnectionManager connectionMgr, final ChannelHandlerContext ctx,
			final ChannelStateEvent e ) throws Exception {

		logger.info( "ChannelConnected: parent=" + e.getChannel().getParent() + ", channel=" + e.getChannel() );

		channels.add( e.getChannel() );

		Channels.write( e.getChannel(),
				new NodeIdentificationMessage( localNode.getNodeId(), connectionMgr.getServerAddress() ) );

		// Send join request if we connect as client
		if ( e.getChannel().getFactory() instanceof ClientSocketChannelFactory ) {
			if ( !joinRequestFilter.contains( e.getChannel().getRemoteAddress() ) ) {
				logger.info( "We connected to a stranger. Request join." );
				JoinRequestMessage request;
				request = new JoinRequestMessage( localNode.getNodeId(), connectionMgr.getServerAddress() );
				Channels.write( e.getChannel(), request );
			} else {
				joinRequestFilter.remove( e.getChannel().getRemoteAddress() );
			}
		}
		super.channelConnected( connectionMgr, ctx, e );
	}

	@Override
	public void channelClosed( final IConnectionManager connectionMgr, final ChannelHandlerContext ctx,
			final ChannelStateEvent e ) throws Exception {

		// remove closed channels from lookup table
		HashAndAddress address = getLookupTable().remove( e.getChannel() );

		// check if it was our successor
		if ( e.getChannel().equals( getSuccessorChannel() ) ) {
			logger.info( "Channel " + e.getChannel() + " to our successor " + getSuccessorId() + " has been closed." );

			LookupResult result = getLookupTable().lookup( getSuccessorId() );
			// check if we got another channel otherwise we'll select the next
			// higher
			if ( result != null ) {
				logger.info( "New successor will be " + result.getHash() + " at " + result.getChannel() );
				changeSuccessor( result.getHash(), result.getChannel() );
			} else {
				// no other peer connected - falling back to single mode
				logger.info( "No other peer connected - falling back to single mode" );
				changeSuccessor( localNode.getNodeId(), null );
				changePredecessor( localNode.getNodeId(), null );
			}
			// also query for our successor
			queryForSuccessor();
		}

		super.channelClosed( connectionMgr, ctx, e );
	}

	@Override
	public void messageReceived( final IConnectionManager connectionMgr, final ChannelHandlerContext ctx,
			final MessageEvent e ) throws Exception {

		// Filter junk
		Object message = e.getMessage();
		if ( !(message instanceof AbstractMessage) ) {
			logger.warn( "Discarding unknown message type: " + message );
			return;
		}
		AbstractMessage msg = (AbstractMessage) message;

		// We don't care if this is a response to our lookup or someone else's
		if ( msg instanceof SuccessorFoundMessage ) {
			completeFutures( (SuccessorFoundMessage) msg );
		}

		// Update our knowledge about the channel
		if ( msg instanceof NodeIdentificationMessage ) {
			IHash nodeId = ((NodeIdentificationMessage) msg).getNodeId();
			getLookupTable().put( nodeId, e.getChannel() );
			// maybe we find a new successor through the new connection
			queryForSuccessor();
		}

		// check if we have to route the message
		if ( msg.isRoutable() && !getResponsibility().contains( msg.getReceiver() ) ) {
			routeMessage( msg );
			return;
		}

		// message is destined for us
		if ( message instanceof FindSuccessorMessage ) {
			handleFindSuccessorMessage( (FindSuccessorMessage) message );
		} else if ( message instanceof ChordEnvelope ) {
			ChordEnvelope envelope = (ChordEnvelope) message;
			fireMessageReceived( envelope.getSenderId(), envelope.getContent() );
		} else if ( message instanceof WelcomeMessage ) {
			handleWelcomeMessage( e, (WelcomeMessage) message );
		} else if ( message instanceof IAmYourPredeccessor ) {
			handleIAmYourPredeccessorMessage( e, (IAmYourPredeccessor) message );
		} else if ( message instanceof JoinRequestMessage ) {
			handleJoinRequestMessage( connectionMgr, (JoinRequestMessage) message );
		} else if ( message instanceof NodeListMessage ) {
			handleNodeList( (NodeListMessage) message );
		}

		super.messageReceived( connectionMgr, ctx, e );
	}

	private void handleNodeList( final NodeListMessage message ) {
		logger.info( "Got propagation list " + message );
		for ( HashAndAddress haa : message.getKnownNodes() ) {
			getOrCreateChannel( haa, haa.getAddress() );
		}
	}

	private void handleJoinRequestMessage( final IConnectionManager connectionMgr2, final JoinRequestMessage message ) {
		if ( message.getNodeId().equals( getPredecessorId() ) ) {
			// the node is already our predecessor - no reason to join again
			return;
		}
		welcomeNode( connectionMgr, message.getNodeId(), message.getAddress() );
	}

	private void handleIAmYourPredeccessorMessage( final MessageEvent e, final IAmYourPredeccessor msg ) {
		changePredecessor( msg.getNodeId(), e.getChannel() );
	}

	private void handleWelcomeMessage( final MessageEvent e, final WelcomeMessage msg ) {

		getLookupTable().put( msg.getSuccessorId(), e.getChannel() );

		logger.info( "Joining chord ring. My successor will be " + msg.getSuccessorId() + " at "
				+ msg.getSuccessorAddress() );

		changeSuccessor( msg.getSuccessorId(), e.getChannel() );
	}

	/**
	 * Invoked when a {@link FindSuccessorMessage} has been received. It
	 * responds with a {@link SuccessorFoundMessage} if we are the successor and
	 * forwards the message to our successor if not.
	 * 
	 * @param e
	 * @param msg
	 */
	private void handleFindSuccessorMessage( final FindSuccessorMessage msg ) {

		logger.debug( "I'm the successor: " + getResponsibility() + " contained " + msg.getKey() );
		SuccessorFoundMessage responseMsg;
		responseMsg = new SuccessorFoundMessage( msg.getOrigin(), msg.getKey(), localNode.getNodeId(),
				connectionMgr.getServerAddress() );
		routeMessage( responseMsg );
	}

	private void welcomeNode( final IConnectionManager connectionMgr, final IHash nodeId, final SocketAddress address ) {

		// mark the new connection so that no "JoinRequest" will be sent
		joinRequestFilter.add( address );

		logger.info( "Connecting to new node " + address + " to welcome it." );
		Channel channel = getOrCreateChannel( nodeId, address );
		sendWelcomeMessage( connectionMgr, nodeId, address, channel );
	}

	private Channel getOrCreateChannel( final IHash hash, final SocketAddress address ) {
		Channel channel = getLookupTable().getSingle( hash );
		if ( channel != null ) {
			return channel;
		}
		if ( hash.equals( localNode.getNodeId() ) ) {
			return null;
		}

		ConnectionFuture future = connectionMgr.connectTo( address );
		future.awaitUninterruptibly();
		return future.getChannel();
	}

	private void sendWelcomeMessage( final IConnectionManager connectionMgr, final IHash nodeId,
			final SocketAddress address, final Channel channel ) {
		logger.info( "Connected to " + address + " sending welcome message." );
		// Send welcome message
		SocketAddress serverAddress = connectionMgr.getServerAddress();
		WelcomeMessage welcomeMsg;
		welcomeMsg = new WelcomeMessage( localNode.getNodeId(), serverAddress, getResponsibility().getLowerBound() );

		Channels.write( channel, welcomeMsg );

		// Accept our new predecessor
		// changePredecessor( nodeId, channel );
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
					future.complete( new HashAndAddress( msg.getSuccessor(), msg.getServerAddress() ) );
					// Don't abort since there might be multiple pending lookups
				}
			}
		}
	}

	private void queryForSuccessor() {
		ObservableFuture<IHash> future = findSuccessor( localNode.getNodeId().getNext() );
		future.addFutureListener( new FutureListener<IHash>() {

			@Override
			public void completed( final ObservableFuture<IHash> future ) {
				try {
					HashAndAddress haa = (HashAndAddress) future.get();
					if ( future.isDone() && !future.isCancelled() ) {
						changeSuccessor( haa, getOrCreateChannel( haa, haa.getAddress() ) );
					}
				} catch ( InterruptedException e ) {
					logger.warn( "Interrupted during successor lookup.", e );
				} catch ( ExecutionException e ) {
					logger.error( "Successor lookup failed.", e );
				}
			}
		} );
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
		if ( getResponsibility().contains( key ) ) {
			logger.debug( "LocalNode is the successor: " + getResponsibility() + " contained " + key );
			// Local node is the successor
			future.complete( new HashAndAddress( localNode.getNodeId(), connectionMgr.getServerAddress() ) );
			return future;
		}
		// Send a lookup message to our successor
		// Channels.write( successorChannel, );
		routeMessage( new FindSuccessorMessage( localNode.getNodeId(), key ) );
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
	public void sendMessage( final Serializable message, final IHash nodeId ) {
		logger.debug( "Sending message " + message + " to " + nodeId );
		if ( getResponsibility().contains( nodeId ) ) {

			logger.debug( "Message " + message + " passed through loopback." );
			fireMessageReceived( localNode.getNodeId(), message );

			return;
		}
		routeMessage( new ChordEnvelope( localNode.getNodeId(), nodeId, message ) );
	}

	private void routeMessage( final AbstractMessage msg ) {
		if ( getLookupTable().isEmpty() ) {
			logger.warn( "Lookup table is empty. Discarding " + msg );
			return;
		}

		LookupResult lookupResult = getLookupTable().lookup( msg.getReceiver() );
		if ( lookupResult != null ) {
			Channels.write( lookupResult.getChannel(), msg );
		}
	}

	@Override
	public String getName() {
		return "ChordOverlayHandler";
	}

	@Override
	public void init( final IConnectionManager conMgr ) {
		connectionMgr = conMgr;
		// Configure stabilization of successor and fingers
		scheduler.scheduleAtFixedRate( new Runnable() {

			@Override
			public void run() {
				stabilize();
			}
		}, stabilizationRate );
		scheduler.scheduleAtFixedRate( new Runnable() {

			@Override
			public void run() {
				notifySuccessorAboutUs();
			}
		}, notificationRate );
		scheduler.scheduleAtFixedRate( new Runnable() {

			@Override
			public void run() {
				propagateKnownNodes();
			}
		}, propagationDelay );
	}

	private void propagateKnownNodes() {
		Set<HashAndAddress> nodes = getLookupTable().listNodes();
		for ( Channel channel : channels ) {
			if ( channel.isConnected() ) {
				Channels.write( channel, new NodeListMessage( nodes ) );
			}
		}
	}

	private void notifySuccessorAboutUs() {
		if ( getSuccessorChannel() != null && getSuccessorChannel().isConnected() ) {
			Channels.write( getSuccessorChannel(), new IAmYourPredeccessor( localNode.getNodeId() ) );
		}
	}

	public void stabilize() {
		ObservableFuture<IHash> future = findSuccessor( localNode.getNodeId().getNext() );
		future.addFutureListener( new FutureListener<IHash>() {

			@Override
			public void completed( final ObservableFuture<IHash> future ) {
				try {
					HashAndAddress hash = (HashAndAddress) future.get();
					if ( hash != null ) {
						changeSuccessor( hash, getOrCreateChannel( hash, hash.getAddress() ) );
						logger.info( "Stabilization successful." );
					}
				} catch ( InterruptedException e ) {
					logger.warn( "Stabilization interrupted.", e );
				} catch ( ExecutionException e ) {
					logger.error( "Stabilization failed.", e );
				}
			}
		} );
	}
}
