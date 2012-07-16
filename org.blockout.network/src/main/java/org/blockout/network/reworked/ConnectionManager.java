package org.blockout.network.reworked;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ChildChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolver;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class ConnectionManager extends SimpleChannelHandler implements IConnectionManager {

	private static final Logger		logger;
	static {
		logger = LoggerFactory.getLogger( ConnectionManager.class );
	}

	private final ChannelGroup		allChannels;
	private ClientBootstrap			clientBootstrap;
	private ServerBootstrap			serverBootstrap;
	private Channel					serverChannel;
	private final PipelineFactory	pipelineFactory;

	public ConnectionManager() {
		allChannels = new DefaultChannelGroup();
		pipelineFactory = new PipelineFactory();
	}

	public ConnectionManager(final ChannelInterceptor... interceptors) {
		allChannels = new DefaultChannelGroup();
		pipelineFactory = new PipelineFactory();
		if ( interceptors != null && interceptors.length > 0 ) {
			for ( ChannelInterceptor interceptor : interceptors ) {
				addChannelInterceptor( interceptor );
			}
		}
	}

	public void init() {
		initClient();
		initServer();
	}

	private void initClient() {
		clientBootstrap = new ClientBootstrap( new NioClientSocketChannelFactory() );
		clientBootstrap.setPipelineFactory( pipelineFactory );
	}

	private void initServer() {
		serverBootstrap = new ServerBootstrap( new NioServerSocketChannelFactory() );
		serverBootstrap.setPipelineFactory( pipelineFactory );
		serverChannel = serverBootstrap.bind( new InetSocketAddress( findLocalNetworkInterface(), 0 ) );
		logger.info( "Bound server to " + serverChannel.getLocalAddress() );
	}

	private InetAddress findLocalNetworkInterface() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while ( interfaces.hasMoreElements() ) {
				NetworkInterface networkInterface = interfaces.nextElement();
				if ( !networkInterface.isUp() ) {
					logger.debug( "Skipped (down): " + networkInterface );
					continue;
				}
				if ( networkInterface.isLoopback() ) {
					logger.debug( "Skipped loopback: " + networkInterface );
					continue;
				}
				if ( networkInterface.isPointToPoint() ) {
					logger.debug( "Skipped ptp: " + networkInterface );
					continue;
				}
				Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
				while ( inetAddresses.hasMoreElements() ) {
					InetAddress address = inetAddresses.nextElement();
					if ( address.isLoopbackAddress() ) {
						logger.debug( "Skipped loopback: " + address );
						continue;
					}
					if ( address.isMCNodeLocal() ) {
						logger.debug( "Skipped node local: " + address );
						continue;
					}
					if ( address instanceof Inet6Address ) {
						logger.debug( "Skipped V6 address: " + address );
						continue;
					}
					logger.debug( "Selected: " + address + " of " + networkInterface );
					return address;
				}
			}
		} catch ( SocketException e ) {
			throw new RuntimeException( "No appropriate network interface found.", e );
		}
		throw new RuntimeException( "No appropriate network interface found." );
	}

	public void addChannelInterceptor( final ChannelInterceptor interceptor ) {
		pipelineFactory.addChannelInterceptor( interceptor );
	}

	@Override
	public SocketAddress getServerAddress() {
		return serverChannel.getLocalAddress();
	}

	@Override
	public Set<SocketAddress> listConnections() {
		Set<SocketAddress> connections = new HashSet<SocketAddress>();
		for ( Channel channel : allChannels ) {
			connections.add( channel.getRemoteAddress() );
		}
		return connections;
	}

	@Override
	public ConnectionFuture connectTo( final SocketAddress address ) {
		logger.info( "Connecting to " + address );
		Channel channel = findChannel( address );
		if ( channel != null ) {
			logger.debug( "Channel already opened to " + address );
			return new ChannelFutureAdapter( Channels.succeededFuture( channel ) );
		}
		ChannelFuture future = clientBootstrap.connect( address );
		future.addListener( new ChannelFutureListener() {

			@Override
			public void operationComplete( final ChannelFuture future ) throws Exception {
				if ( !future.isSuccess() ) {
					logger.warn( "Failed to connect to " + future.getChannel().getRemoteAddress(), future.getCause() );
				}
				logger.info( "Connected to " + future.getChannel().getRemoteAddress() );
			}
		} );
		return new ChannelFutureAdapter( future );
	}

	@Override
	public void disconnectFrom( final SocketAddress address ) {
		logger.info( "Disconnecting from " + address );
		Channel channel = findChannel( address );
		if ( channel == null ) {
			return;
		}
		channel.close();
	}

	private Channel findChannel( final SocketAddress address ) {
		for ( Channel channel : allChannels ) {
			if ( channel.getRemoteAddress().equals( address ) ) {
				return channel;
			}
		}
		return null;
	}

	@Override
	public void exceptionCaught( final ChannelHandlerContext ctx, final ExceptionEvent e ) throws Exception {
		logger.warn( "Caught exception in network stack.", e.getCause() );
		super.exceptionCaught( ctx, e );
	}

	@Override
	public void channelConnected( final ChannelHandlerContext ctx, final ChannelStateEvent e ) throws Exception {
		logger.info( "ChannelConnected to " + e.getChannel().getRemoteAddress() );
		super.channelConnected( ctx, e );
	}

	@Override
	public void channelDisconnected( final ChannelHandlerContext ctx, final ChannelStateEvent e ) throws Exception {
		logger.info( "Disconnected from " + e.getChannel().getRemoteAddress() );
		e.getChannel().close();
		super.channelDisconnected( ctx, e );
	}

	@Override
	public void childChannelOpen( final ChannelHandlerContext ctx, final ChildChannelStateEvent e ) throws Exception {
		logger.info( "Client " + e.getChannel().getRemoteAddress() + " connected." );
		allChannels.add( e.getChannel() );
		super.childChannelOpen( ctx, e );
	}

	@Override
	public void childChannelClosed( final ChannelHandlerContext ctx, final ChildChannelStateEvent e ) throws Exception {
		logger.info( "Client " + e.getChannel().getRemoteAddress() + " disconnected." );
		super.childChannelClosed( ctx, e );
	}

	private class PipelineFactory implements ChannelPipelineFactory {

		private final List<ChannelInterceptor>	interceptors;

		public PipelineFactory() {
			interceptors = Lists.newArrayList();
		}

		public void addChannelInterceptor( final ChannelInterceptor interceptor ) {
			interceptors.add( interceptor );
		}

		@Override
		public ChannelPipeline getPipeline() throws Exception {
			ChannelPipeline pipeline = Channels.pipeline();
			pipeline.addLast( "connectionManager", ConnectionManager.this );
			pipeline.addLast( "objectEncoder", new ObjectEncoder() );
			ClassResolver classResolver = ClassResolvers.cacheDisabled( getClass().getClassLoader() );
			pipeline.addLast( "objectDecoder", new ObjectDecoder( classResolver ) );
			for ( ChannelInterceptor interceptor : interceptors ) {
				pipeline.addLast( interceptor.getName(), new ChannelHandlerInterceptorAdapter( ConnectionManager.this,
						interceptor ) );
			}
			return pipeline;
		}
	}
}
