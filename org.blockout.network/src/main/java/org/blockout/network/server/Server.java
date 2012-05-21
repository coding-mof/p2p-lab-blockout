package org.blockout.network.server;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.blockout.network.channel.Channel;
import org.blockout.network.channel.IChannel;
import org.blockout.network.channel.ChannelInterest;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.google.common.base.Preconditions;

public class Server implements IServer {
	protected CopyOnWriteArraySet<IConnectionListener> listeners;
	protected SimpleChannelHandler newConnectionHandler;

	private ServerBootstrap bootstrap;
	private ChannelPipeline pipeline;
	
	private int additionalHandlerCount;
	
	
	public Server(ServerBootstrap bootstrap) {
		Preconditions.checkNotNull(bootstrap);
		
		this.listeners = new CopyOnWriteArraySet<IConnectionListener>();
		this.additionalHandlerCount = 0;
		
		
		this.newConnectionHandler = new SimpleChannelHandler(){
			public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
				IChannel channel = new Channel(ChannelInterest.CLIENT, ctx.getChannel());
				for(IConnectionListener listener: listeners){
					listener.notify(channel);
				}
			}
		};
		
		this.bootstrap = bootstrap;
		
		this.bootstrap.setPipelineFactory(new ChannelPipelineFactory(){
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline();
			}
		});
		
		this.pipeline = this.bootstrap.getPipeline();
		this.pipeline.addLast("newConnection", this.newConnectionHandler);
		
		this.bootstrap.bind(new InetSocketAddress(6423));
	}

	@Override
	public void addHandler(SimpleChannelHandler handler) {
		this.pipeline.addBefore("newConnection", "additionalHandler"+this.additionalHandlerCount, handler);
		this.additionalHandlerCount++;		
	}

	@Override
	public void addListener(IConnectionListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void addListener(Set<IConnectionListener> listeners) {
		this.listeners.addAll(listeners);
	}
}
