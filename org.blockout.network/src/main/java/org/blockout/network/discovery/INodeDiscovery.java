package org.blockout.network.discovery;

import java.util.List;

import org.blockout.network.INodeAddress;

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
	public List<INodeAddress> listNodes();

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
