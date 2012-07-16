package org.blockout.network.discovery;

import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;

import org.blockout.network.LocalNode;

import com.google.common.collect.Lists;

/**
 * Trivial implementation of the node discovery.
 * 
 * @author Marc-Christian Schulze
 * 
 */

public class DiscoveryStub implements INodeDiscovery {

	protected CopyOnWriteArrayList<DiscoveryListener>	listener;
	protected List<SocketAddress>						knownNodes;

	@Inject
	public DiscoveryStub(final LocalNode localNode) {
		listener = new CopyOnWriteArrayList<DiscoveryListener>();
		knownNodes = Lists.newArrayList();

		// knownNodes.add( localNode.getInfo() );
	}

	@Override
	public List<SocketAddress> listNodes() {
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
