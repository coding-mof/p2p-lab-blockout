package org.blockout.network.netty;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executors;

import org.blockout.network.ConnectionManager;
import org.blockout.network.INodeAddress;
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

import com.google.common.base.Preconditions;

public class NettyConnectionManager extends SimpleChannelUpstreamHandler
		implements ConnectionManager {
	private ServerBootstrap serverBootstrap;
	private ClientBootstrap clientBootstrap;
	private NettyChannelPipelineFactory pipelineFactory;

	private final HashMap<INodeAddress, Channel> channels;
	private int serverPort;
	private InetSocketAddress address;
	private IMessagePassing mp;

	public NettyConnectionManager(int serverPort) {
		this.channels = new HashMap<INodeAddress, Channel>();
		this.serverPort = serverPort;
	}

	public void setMp(IMessagePassing mp) {
		this.mp = mp;
	}

	public void setPipelineFactory(NettyChannelPipelineFactory pipelineFactory) {
		this.pipelineFactory = pipelineFactory;
	}

	public void setServerBootstrap(ServerBootstrap serverBootstrap) {
		this.serverBootstrap = serverBootstrap;
	}

	public void setClientBootstrap(ClientBootstrap clientBootstrap) {
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
		return this.serverPort;
	}

	@Override
	public InetSocketAddress getAddress() {
		return this.address;
	}

	@Override
	public Set<Channel> getAllConnections() {
		HashSet<Channel> set = new HashSet<Channel>(this.channels.values());
		return set;
	}

	public void setUp() {
		this.serverBootstrap.setOption("child.keepAlive", true);
		this.serverBootstrap.setOption("keepAlive", true);

		this.pipelineFactory.addLast("NettyConnectionManager", this);

		this.serverBootstrap.setPipelineFactory(this.pipelineFactory);
		this.clientBootstrap.setPipelineFactory(this.pipelineFactory);

		// Start Server
		Channel serverChannel = this.serverBootstrap
				.bind(new InetSocketAddress(this.serverPort));
		this.address = (InetSocketAddress) serverChannel.getLocalAddress();
		this.serverPort = this.address.getPort();
	}

	@Override
	public Channel getConnection(INodeAddress address) {
		if (this.channels.containsKey(address)) {
			Channel chan = this.channels.get(address);
			if (chan.isOpen()) {
				return chan;
			} else {
				this.channels.remove(address);
				return this.openConnection(address);
			}
		} else {
			return this.openConnection(address);
		}
	}

	private Channel openConnection(INodeAddress address) {
		int i = 0;
		Channel chan;
		do {
			chan = this.createConnection(address);
		} while (!chan.isOpen() || i++ <= 3);

		if (!chan.isOpen()) {
			return null;
		}

		this.channels.put(address, chan);
		return chan;
	}

	private Channel createConnection(INodeAddress address) {
		Preconditions.checkNotNull(address);
		Preconditions.checkNotNull(address.getInetAddress());
		// Make a new connection.
		ChannelFuture connectFuture = this.clientBootstrap.connect(address
				.getInetAddress());

		// Wait until the connection is made successfully.
		Channel channel = connectFuture.awaitUninterruptibly().getChannel();

		return channel;
	}

	@Override
	public void closeConnection(INodeAddress address) {
		Channel chan;
		if (this.channels.containsKey(address)) {
			chan = this.channels.get(address);
			this.channels.remove(address);
			if (chan.isOpen()) {
				chan.close();
			}
		}
	}

	@Override
	public void closeConnection(Channel channel) {
		if (this.channels.containsValue(channel)) {
			for (Entry<INodeAddress, Channel> entry : this.channels.entrySet()) {
				if (entry.getValue() == channel) {
					this.channels.remove(entry.getKey());
				}
			}
		}
		if (channel.isOpen()) {
			channel.close();
		}
	}

	// Netty Event Handling
	/**
	 * Invoked when a message object (e.g: {@link ChannelBuffer}) was received
	 * from a remote peer.
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e)
			throws Exception {
		final NettyConnectionManager that = this;
		Runnable handleMessage = new Runnable() {
			@Override
			public void run() {
				that.mp.messageReceived(e);
			}
		};
		Executors.newCachedThreadPool().execute(handleMessage);
		ctx.sendUpstream(e);
	}

	/**
	 * Invoked when an exception was raised by an I/O thread or a
	 * {@link ChannelHandler}.
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		if (this == ctx.getPipeline().getLast()) {
			e.getCause().printStackTrace();
			this.closeConnection(e.getChannel());
		}
		ctx.sendUpstream(e);
	}

	/**
	 * Invoked when a {@link Channel} was disconnected from its remote peer.
	 */
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		this.closeConnection(e.getChannel());
		ctx.sendUpstream(e);
	}

	/**
	 * Invoked when a {@link Channel} was closed and all its related resources
	 * were released.
	 */
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		this.closeConnection(e.getChannel());
		ctx.sendUpstream(e);
	}

	/**
	 * Invoked when a child {@link Channel} was closed. (e.g. the accepted
	 * connection was closed)
	 */
	@Override
	public void childChannelClosed(ChannelHandlerContext ctx,
			ChildChannelStateEvent e) throws Exception {
		this.closeConnection(e.getChannel());
		ctx.sendUpstream(e);
	}

}
