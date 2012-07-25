package org.blockout.network.reworked;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.blockout.network.dht.IHash;
import org.blockout.network.dht.WrappedRange;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;

import com.google.common.collect.TreeMultimap;

public class LookupTable {
	private final TreeMultimap<IHash, Channel>	table;
	private final TreeSet<IHash>				keys;

	public LookupTable() {
		table = TreeMultimap.create();
		keys = new TreeSet<IHash>();
	}

	public boolean isEmpty() {
		synchronized ( table ) {
			return table.isEmpty();
		}
	}

	public void put( final IHash hash, final Channel c ) {
		synchronized ( table ) {
			table.put( hash, c );
			keys.add( hash );
		}
	}

	public boolean containsAnyOf( final WrappedRange<IHash> range ) {
		synchronized ( table ) {
			for ( IHash hash : table.keySet() ) {
				if ( range.contains( hash ) ) {
					return true;
				}
			}

			return false;
		}
	}

	public Set<HashAndAddress> listNodes() {
		Set<HashAndAddress> nodes = new HashSet<HashAndAddress>();
		for ( IHash hash : keys ) {
			for ( Channel c : table.get( hash ) ) {
				if ( c.getFactory() instanceof ClientSocketChannelFactory ) {
					nodes.add( new HashAndAddress( hash, c.getRemoteAddress() ) );
				}
			}
		}
		return nodes;
	}

	public Channel getSingle( final IHash hash ) {
		synchronized ( table ) {
			SortedSet<Channel> sortedSet = table.get( hash );
			if ( sortedSet.isEmpty() ) {
				return null;
			}
			return sortedSet.first();
		}
	}

	public LookupResult lookup( final IHash hash ) {
		synchronized ( table ) {
			if ( table.isEmpty() ) {
				return null;
			}
			IHash higherKey = keys.ceiling( hash );
			if ( higherKey == null ) {
				// There is no higher key, so we need to wrap around to the
				// first in the ring
				return new LookupResult( keys.first(), getSingle( keys.first() ) );
			} else {
				// Take next higher key to route
				return new LookupResult( higherKey, getSingle( higherKey ) );
			}
		}
	}

	public SortedSet<Channel> getAll( final IHash hash ) {
		synchronized ( table ) {
			return table.get( hash );
		}
	}

	public HashAndAddress remove( final Channel c ) {
		synchronized ( table ) {
			Set<IHash> hashes = new HashSet<IHash>();
			IHash rhash = null;
			Iterator<Entry<IHash, Channel>> iterator = table.entries().iterator();
			while ( iterator.hasNext() ) {
				Entry<IHash, Channel> entry = iterator.next();
				if ( entry.getValue().equals( c ) ) {
					rhash = entry.getKey();
					hashes.add( entry.getKey() );
					iterator.remove();
				}
			}
			for ( IHash hash : hashes ) {
				if ( table.get( hash ).size() == 0 ) {
					keys.remove( hash );
				}
			}
			return new HashAndAddress( rhash, c.getRemoteAddress() );
		}
	}
}
