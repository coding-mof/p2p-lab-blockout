package org.blockout.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Named;

/**
 * Trivial implementation of the node discovery.
 * 
 * @author Marc-Christian Schulze
 * 
 */
@Named
public class DiscoveryStub implements INodeDiscovery {

	protected CopyOnWriteArrayList<DiscoveryListener>	listener;
	protected List<NodeInfo>							knownNodes;

	public DiscoveryStub() {
		listener = new CopyOnWriteArrayList<DiscoveryListener>();
		knownNodes = new ArrayList<NodeInfo>();

		try {
			knownNodes.add( new NodeInfo( InetAddress.getLocalHost() ) );
			knownNodes.add( new NodeInfo( InetAddress.getByName( "127.0.0.1" ) ) );
		} catch ( UnknownHostException e ) {
			e.printStackTrace();
		}
	}

	@Override
	public List<NodeInfo> listNodes() {
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
