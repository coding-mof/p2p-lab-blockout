package org.blockout.network.discovery;

import java.net.SocketAddress;

/**
 * Implement this interface to get notified about newly discovered nodes.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public interface DiscoveryListener {
	/**
	 * Gets invoked when a new node has been discovered in the network layer.
	 * 
	 * @param info
	 *            The information about the new node.
	 */
	public void nodeDiscovered( SocketAddress nodeAddress );
}
