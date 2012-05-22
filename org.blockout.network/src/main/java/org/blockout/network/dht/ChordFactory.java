package org.blockout.network.dht;

import org.blockout.network.dht.IDHTFactory;
import org.blockout.network.server.IServer;

public class ChordFactory implements IDHTFactory {

	@Override
	public IDistributedHashTable createDHT(IServer server) {
		// Try to find other nodes
		// Send out UDP Broadcast
		// Wait for at least one answer
		// Found a Node!
		// Timed-out
		// Generate node ID
		// Ask Node for my successor
		// Timed-out
		// Connect to Successor
		// Timed-out
		// Create Chord Object with its ID and Successor

		// The Chord Object will inject itself as a Handler into the server 
		// object as a handler and start its own UDP Broadcast listener
		return null;
	}

}
