package org.blockout.network;

import java.net.InetSocketAddress;
import java.util.Set;

import org.blockout.network.dht.IHash;
import org.blockout.network.discovery.INodeAddress;
import org.jboss.netty.channel.Channel;

public interface ConnectionManager {
	public Channel getConnection(INodeAddress address);

	public void closeConnection(INodeAddress address);

	public void closeConnection(Channel channel);
	
	public InetSocketAddress getAddress();

	public Set<INodeAddress> getAllConnections();

	public void addConnection(INodeAddress address, Channel channel);

	public Channel getConnection(IHash nodeId);
}
