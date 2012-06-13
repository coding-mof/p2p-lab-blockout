package org.blockout.network.netty;

import java.net.InetSocketAddress;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

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

public class NettyConnectionManager extends SimpleChannelUpstreamHandler implements ConnectionManager {

	private static final Logger								logger;
	static {
		logger = LoggerFactory.getLogger( NettyConnectionManager.class );
	}

	private final ServerBootstrap							serverBootstrap;
	private final ClientBootstrap							clientBootstrap;
	private final NettyChannelPipelineFactory				pipelineFactory;

	private final ConcurrentHashMap<INodeAddress, Channel>	channels;
	private int												serverPort;
	private InetSocketAddress								address;
	private IMessagePassing									mp;

	private boolean											socketKeepAlive			= true;
	private boolean											serverSocketKeepAlive	= true;
	private final ExecutorService							threadPool;

	public NettyConnectionManager(final int serverPort, final ExecutorService threadPool,
			final NettyChannelPipelineFactory pipelineFactory, final ServerBootstrap serverBootstrap,
			final ClientBootstrap clientBootstrap) {
		this.threadPool = threadPool;
		channels = new ConcurrentHashMap<INodeAddress, Channel>();
		this.serverPort = serverPort;

		this.pipelineFactory = pipelineFactory;
		this.serverBootstrap = serverBootstrap;
		this.clientBootstrap = clientBootstrap;

		setUp();
	}

	public boolean isSocketKeepAlive() {
		return socketKeepAlive;
	}

	public void setSocketKeepAlive( final boolean socketKeepAlive ) {
		this.socketKeepAlive = socketKeepAlive;
	}

	public boolean isServerSocketKeepAlive() {
		return serverSocketKeepAlive;
	}

	public void setServerSocketKeepAlive( final boolean serverSocketKeepAlive ) {
		this.serverSocketKeepAlive = serverSocketKeepAlive;
	}

	public void setMp( final IMessagePassing mp ) {
		this.mp = mp;
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

	public void setUp() {
		serverBootstrap.setOption( "child.keepAlive", isSocketKeepAlive() );
		serverBootstrap.setOption( "keepAlive", isServerSocketKeepAlive() );

		pipelineFactory.addLast( "NettyConnectionManager", this );

		serverBootstrap.setPipelineFactory( pipelineFactory );
		clientBootstrap.setPipelineFactory( pipelineFactory );

		// Start Server
		Channel serverChannel = serverBootstrap.bind( new InetSocketAddress( serverPort ) );
		address = (InetSocketAddress) serverChannel.getLocalAddress();
		serverPort = address.getPort();
	}

	@Override
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
	public Channel getConnection( final IHash nodeId ) {
		return channels.get( nodeId );
	}

	private Channel openConnection( final INodeAddress address ) {
		int i = 0;
		Channel chan;
		do {
			chan = createConnection( address );
		} while ( !chan.isOpen() && i++ <= 1 );

		if ( !chan.isOpen() ) {
			return null;
		}

		channels.put( address, chan );
		return chan;
	}

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
		Runnable handleMessage = new Runnable() {
			@Override
			public void run() {
				mp.messageReceived( e );
			}
		};

		threadPool.execute( handleMessage );
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
