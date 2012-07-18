package org.blockout.network.reworked;

import java.net.SocketAddress;
import java.util.Set;

public interface IConnectionManager {

	public SocketAddress getServerAddress();

	public ConnectionFuture connectTo( final SocketAddress address );

	public void disconnectFrom( final SocketAddress address );

	public Set<SocketAddress> listConnections();

	public void addConnectionListener( ConnectionListener l );

	public void removeConnectionListener( ConnectionListener l );
}