package org.blockout.network.channel;

import java.util.Set;

import org.blockout.network.INetworkEvent;
import org.jboss.netty.channel.Channel;

public interface IChannel {
	public void addListener(IChannelListener listener);
	public void addListener(Set<IChannelListener> listeners);
	
	public void send(INetworkEvent<?> event);
	
	public Set<ChannelInterest> getInterest();
	public void addInterest(ChannelInterest interest);
	public void addInterest(Set<ChannelInterest> interest);
	
	public Channel getChannel();
}
