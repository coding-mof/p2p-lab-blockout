package org.blockout.network.discovery;

import java.net.SocketAddress;
import java.util.List;

/**
 * Implementations are responsible for auto detecting other nodes running the
 * same program in the same version.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public interface INodeDiscovery {
	/**
	 * Returns a list of known nodes in the network or an empty list if no nodes
	 * are currently known.
	 * 
	 * @return A list of known nodes in the network or an empty list if no nodes
	 *         are currently known.
	 */
	public List<SocketAddress> listNodes();

	/**
	 * Attaches a new listener.
	 * 
	 * @param l
	 *            The listener to attach.
	 */
	public void addDiscoveryListener( DiscoveryListener l );

	/**
	 * Removes a listener.
	 * 
	 * @param l
	 *            The listener to detach.
	 */
	public void removeDiscoveryListener( DiscoveryListener l );
}
