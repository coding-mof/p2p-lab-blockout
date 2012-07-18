package org.blockout.network.reworked;

import java.net.SocketAddress;

/**
 * Implement this interface to get notified about the internal connections
 * managed by the {@link IConnectionManager}.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public interface ConnectionListener {

	/**
	 * Gets invoked when we have successfully established a connection as
	 * client.
	 * 
	 * @param connectionMgr
	 *            A reference to the connection manager.
	 * @param localAddress
	 *            The local address of this connection.
	 * @param remoteAddress
	 *            The remote address of this connection.
	 */
	public void connected( IConnectionManager connectionMgr, SocketAddress localAddress, SocketAddress remoteAddress );

	/**
	 * Gets invoked when a connection that we have estabalished has been closed.
	 * 
	 * @param connectionMgr
	 *            A reference to the connection manager.
	 * @param localAddress
	 *            The local address of this connection.
	 * @param remoteAddress
	 *            The remote address of this connection.
	 */
	public void disconnected( IConnectionManager connectionMgr, SocketAddress localAddress, SocketAddress remoteAddress );

	/**
	 * Gets invoked when a client has connected to us.
	 * 
	 * @param connectionMgr
	 *            A reference to the connection manager.
	 * @param localAddress
	 *            The local address of this connection.
	 * @param remoteAddress
	 *            The remote address of this connection.
	 */
	public void clientConnected( IConnectionManager connectionMgr, SocketAddress localAddress,
			SocketAddress remoteAddress );

	/**
	 * Gets invoked when a client has disconnected.
	 * 
	 * @param connectionMgr
	 *            A reference to the connection manager.
	 * @param localAddress
	 *            The local address of this connection.
	 * @param remoteAddress
	 *            The remote address of this connection.
	 */
	public void clientDisconnected( IConnectionManager connectionMgr, SocketAddress localAddress,
			SocketAddress remoteAddress );
}
