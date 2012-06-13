package org.blockout.network.netty;

import java.net.InetSocketAddress;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.blockout.network.ConnectionManager;
import org.blockout.network.INodeAddress;
import org.blockout.network.dht.IHash;
import org.blockout.network.message.IMessagePassing;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ChildChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * The NettyConnectionManager is the only currently available ConnectionManager
 * implementation. And as the Interface defines the use of Channel Classes from
 * the netty package, it will probably stay the only implementation.
 * 
 * It is setup via Spring. Just creating it with new NettyConnectionManager(123)
 * will not work. The Bootstraps, pipeLinefactory and MessagePassing
 * implementation have to be set after the creation of the object, as they
 * depend on each other.
 * 
 * @author Paul Dubs
 * 
 */

public class NettyConnectionManager extends SimpleChannelUpstreamHandler implements ConnectionManager {
	private static final Logger								logger;
	static {
		logger = LoggerFactory.getLogger( NettyConnectionManager.class );
	}
	// Bootstraps and Factory necessary for the creation of listening sockets
	// and new connections
	private ServerBootstrap									serverBootstrap;
	private ClientBootstrap									clientBootstrap;
	private NettyChannelPipelineFactory						pipelineFactory;

	// most accesses will happen more or less concurrently, use a structure that
	// can live with that
	private final ConcurrentHashMap<INodeAddress, Channel>	channels;

	// Where should we listen for new connections?
	private int												serverPort;
	private InetSocketAddress								address;

	// System that will be notified on new Network Messages
	private IMessagePassing									mp;

	/**
	 * First part of the Setup Process
	 * 
	 * @param serverPort
	 */
	public NettyConnectionManager(final int serverPort) {
		channels = new ConcurrentHashMap<INodeAddress, Channel>();
		this.serverPort = serverPort;
	}

	// Mandatory
	public void setMp( final IMessagePassing mp ) {
		this.mp = mp;
	}

	// Mandatory
	public void setPipelineFactory( final NettyChannelPipelineFactory pipelineFactory ) {
		this.pipelineFactory = pipelineFactory;
	}

	// Mandatory
	public void setServerBootstrap( final ServerBootstrap serverBootstrap ) {
		this.serverBootstrap = serverBootstrap;
	}

	// Mandatory
	public void setClientBootstrap( final ClientBootstrap clientBootstrap ) {
		this.clientBootstrap = clientBootstrap;
	}

	public NettyChannelPipelineFactory getPipelineFactory() {
		return pipelineFactory;
	}

	public ServerBootstrap getServerBootstrap() {
		return serverBootstrap;
	}

	public ClientBootstrap getClientBootstrap() {
		return clientBootstrap;
	}

	public int getServerPort() {
		return serverPort;
	}

	@Override
	public InetSocketAddress getAddress() {
		return address;
	}

	@Override
	public Set<INodeAddress> getAllConnections() {
		return channels.keySet();
	}

	// Also Mandatory
	public void setUp() {
		serverBootstrap.setOption( "child.keepAlive", true );
		serverBootstrap.setOption( "keepAlive", true );

		pipelineFactory.addLast( "NettyConnectionManager", this );

		serverBootstrap.setPipelineFactory( pipelineFactory );
		clientBootstrap.setPipelineFactory( pipelineFactory );

		// Start Server
		Channel serverChannel = serverBootstrap.bind( new InetSocketAddress( serverPort ) );
		address = (InetSocketAddress) serverChannel.getLocalAddress();
		serverPort = address.getPort();
	}

	@Override
	/**
	 * Tries to get a connection to the given address. If a connection already
	 * exists, then that connection is returned. If it was closed since it was
	 * used the last time or if no connection at all was found, a new connection
	 * will be created.
	 */
	public Channel getConnection( final INodeAddress address ) {
		if ( channels.containsKey( address ) ) {
			Channel chan = channels.get( address );
			if ( chan.isOpen() ) {
				return chan;
			} else {
				this.closeConnection( address );
				return openConnection( address );
			}
		} else {
			return openConnection( address );
		}
	}

	@Override
	/**
	 * Tries to get a connection to a node that is responsible for the given
	 * nodeId. As the HashMap uses the hashCode() method of the given object
	 * this should find a connection, if we have one already.
	 */
	public Channel getConnection( final IHash nodeId ) {
		// Right now this doesn't use any responsibility range lookups
		return channels.get( nodeId );
	}

	/**
	 * Tries to open a new Connection to the given node.
	 * 
	 * @param address
	 * @return
	 */
	private Channel openConnection( final INodeAddress address ) {
		int i = 0;
		Channel chan;
		do {
			chan = createConnection( address );
		} while ( !chan.isOpen() && i++ <= 1 );
		// Retry functionality is available, but unused
		// (change 1 to x for x tries)

		if ( !chan.isOpen() ) {
			return null;
		}

		channels.put( address, chan );
		return chan;
	}

	/**
	 * Creates a new Connection, and actually waits until it is done.
	 * 
	 * @param address
	 * @return
	 */
	private Channel createConnection( final INodeAddress address ) {
		Preconditions.checkNotNull( address );
		Preconditions.checkNotNull( address.getInetAddress() );
		// Make a new connection.
		logger.debug( "Connecting to: " + address );
		ChannelFuture connectFuture = clientBootstrap.connect( address.getInetAddress() );

		// Wait until the connection is made successfully.
		Channel channel = connectFuture.awaitUninterruptibly().getChannel();
		logger.debug( "Got " + channel );

		return channel;
	}

	@Override
	/**
	 * Closes a connection to the given address and removes it from the
	 * available connections map.
	 */
	public void closeConnection( final INodeAddress address ) {
		logger.debug( "Closing Connection to " + address );
		Channel chan;
		if ( channels.containsKey( address ) ) {
			chan = channels.get( address );
			channels.remove( address );
			if ( chan.isOpen() ) {
				chan.close();
			}
		}

	}

	@Override
	/**
	 * Closes the given connection and removes it from the available connection
	 * map
	 */
	public void closeConnection( final Channel channel ) {
		logger.debug( "Closing Connection to " + channel );
		INodeAddress address = null;
		if ( channels.containsValue( channel ) ) {
			for ( Entry<INodeAddress, Channel> entry : channels.entrySet() ) {
				if ( entry.getValue() == channel ) {
					if ( address == null ) {
						address = entry.getKey();
					}
				}
			}

			channels.remove( address );
		}
		if ( channel.isOpen() ) {
			channel.close();
		}
	}

	@Override
	/**
	 * Adds a new entry in the connection map with the given address as the key
	 */
	public void addConnection( final INodeAddress address, final Channel channel ) {
		if ( !channels.containsKey( address ) ) {
			logger.debug( "Adding Connection to " + address + " Channel: " + channel );
			channels.put( address, channel );
		}
	}

	// Netty Event Handling
	/**
	 * Invoked when a message object (e.g: {@link ChannelBuffer}) was received
	 * from a remote peer.
	 */
	@Override
	public void messageReceived( final ChannelHandlerContext ctx, final MessageEvent e ) throws Exception {
		mp.messageReceived( e );
		ctx.sendUpstream( e );
	}

	/**
	 * Invoked when an exception was raised by an I/O thread or a
	 * {@link ChannelHandler}.
	 */
	@Override
	public void exceptionCaught( final ChannelHandlerContext ctx, final ExceptionEvent e ) throws Exception {
		if ( this == ctx.getPipeline().getLast() ) {
			e.getCause().printStackTrace();
			this.closeConnection( e.getChannel() );
		}
		ctx.sendUpstream( e );
	}

	/**
	 * Invoked when a {@link Channel} was disconnected from its remote peer.
	 */
	@Override
	public void channelDisconnected( final ChannelHandlerContext ctx, final ChannelStateEvent e ) throws Exception {
		this.closeConnection( e.getChannel() );
		ctx.sendUpstream( e );
	}

	/**
	 * Invoked when a {@link Channel} was closed and all its related resources
	 * were released.
	 */
	@Override
	public void channelClosed( final ChannelHandlerContext ctx, final ChannelStateEvent e ) throws Exception {
		this.closeConnection( e.getChannel() );
		ctx.sendUpstream( e );
	}

	/**
	 * Invoked when a child {@link Channel} was closed. (e.g. the accepted
	 * connection was closed)
	 */
	@Override
	public void childChannelClosed( final ChannelHandlerContext ctx, final ChildChannelStateEvent e ) throws Exception {
		this.closeConnection( e.getChannel() );
		ctx.sendUpstream( e );
	}

}
