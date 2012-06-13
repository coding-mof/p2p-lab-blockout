package org.blockout.network;

import java.net.InetSocketAddress;
import java.util.Set;

import org.blockout.network.dht.IHash;
import org.jboss.netty.channel.Channel;

/**
 * A ConnectionManager is used to abstract the creation and destruction of
 * connections away. If a connection is needed to a specific other node it is
 * requested from the ConnectionManager. It can open a new connection, or reuse
 * an old one. Sometimes connections have to be closed explicitly, thus it
 * allows to do that.
 * 
 * It knows its own Address, but it often doesn't know the Address that the
 * sender of a message will be addressed as, so it also allows to add
 * connections to its collection of connections. At this point the abstraction
 * is a bit leaky, in the regard that a different part is responsible for adding
 * some connections.
 * 
 * @author Paul Dubs
 * 
 */

public interface ConnectionManager {
	public InetSocketAddress getAddress();

	public Set<INodeAddress> getAllConnections();

	public void addConnection( INodeAddress address, Channel channel );

	public Channel getConnection( INodeAddress address );

	public Channel getConnection( IHash nodeId );

	public void closeConnection( INodeAddress address );

	public void closeConnection( Channel channel );

}
