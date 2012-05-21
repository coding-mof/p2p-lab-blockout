package org.blockout.network.channel;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.blockout.network.INetworkEvent;
import org.blockout.network.NetworkEvent;
import org.blockout.network.server.IConnectionListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;

public class Channel implements IChannel {
	protected org.jboss.netty.channel.Channel channel;
	protected HashSet<ChannelInterest> interests;
	
	protected CopyOnWriteArraySet<IChannelListener> listeners;
	
	
	public Channel(ChannelInterest interest,
			org.jboss.netty.channel.Channel channel) {		
		this.listeners = new CopyOnWriteArraySet<IChannelListener>();
		
		this.interests = new HashSet<ChannelInterest>();
		this.interests.add(interest);
		
		this.channel = channel;
		ChannelPipeline pipeline = this.channel.getPipeline();
		
		final Channel that = this;
		
		pipeline.addLast("channelEventPropagator",
				new SimpleChannelHandler() {
			@Override
			public void messageReceived(ChannelHandlerContext ctx, MessageEvent e){
				that.notifyListener(ctx, e);
			}
		});
		
	}

	protected void notifyListener(ChannelHandlerContext ctx, MessageEvent e) {
		// TODO: define Message Type 
		INetworkEvent event = new NetworkEvent(
				this.channel.getRemoteAddress(), 
				this.channel.getLocalAddress(),
				e.getMessage());
		for(IChannelListener listener: this.listeners){
			listener.notify(event);
		}
	}

	@Override
	public void addListener(IChannelListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void addListener(Set<IChannelListener> listeners) {
		this.listeners.addAll(listeners);
	}

	@Override
	public void send(INetworkEvent<?> event) {
		this.channel.write(event);
	}

	@Override
	public Set<ChannelInterest> getInterest() {
		return this.interests;
	}

	@Override
	public void addInterest(ChannelInterest interest) {
		this.interests.add(interest);
	}

	@Override
	public void addInterest(Set<ChannelInterest> interest) {
		this.interests.addAll(interest);
	}

	@Override
	public org.jboss.netty.channel.Channel getChannel() {
		return this.channel;
	}

}
