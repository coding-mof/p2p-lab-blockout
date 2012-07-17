package org.blockout.network.message;

import java.io.Serializable;
import java.util.Set;

import org.blockout.network.dht.IHash;

/**
 * Abstractions for a message broker that is able to route messages in the chord
 * ring even if the given key is not directly the node id.
 * 
 * @author Marc-Christian Schulze
 * @author Paul Dubs
 */
public interface IMessagePassing {
	/**
	 * Sends a message to the node that is responsible for the given key.
	 * 
	 * @param msg
	 * @param keyId
	 */
	public void send( Serializable msg, IHash keyId );

	public void addReceiver( IMessageReceiver receiver, Class<?>... filterClass );

	public void addReceiver( Set<IMessageReceiver> receiver, Class<?>... filterClass );

	public void removeReceiver( IMessageReceiver receiver, Class<?>... filterClasses );
}
