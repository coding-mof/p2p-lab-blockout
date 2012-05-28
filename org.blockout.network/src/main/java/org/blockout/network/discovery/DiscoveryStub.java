package org.blockout.network.discovery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;

import org.blockout.network.message.IMessagePassing;

/**
 * Trivial implementation of the node discovery.
 * 
 * @author Marc-Christian Schulze
 * 
 */

public class DiscoveryStub implements INodeDiscovery {

	protected CopyOnWriteArrayList<DiscoveryListener>	listener;
	protected List<INodeAddress> knownNodes;

	@Inject
	public DiscoveryStub(IMessagePassing mp) {
		listener = new CopyOnWriteArrayList<DiscoveryListener>();
		knownNodes = new ArrayList<INodeAddress>();

		knownNodes.add(mp.getOwnAddress());
	}

	@Override
	public List<INodeAddress> listNodes() {
		return knownNodes;
	}

	@Override
	public void addDiscoveryListener( final DiscoveryListener l ) {
		listener.add( l );
	}

	@Override
	public void removeDiscoveryListener( final DiscoveryListener l ) {
		listener.remove( l );
	}
}
