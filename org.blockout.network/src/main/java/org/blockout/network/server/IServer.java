package org.blockout.network.server;

import java.util.Set;

import org.jboss.netty.channel.SimpleChannelHandler; 

public interface IServer {
	public void addHandler(SimpleChannelHandler handler);
	public void addListener(IConnectionListener listener);
	public void addListener(Set<IConnectionListener> listener);
}
