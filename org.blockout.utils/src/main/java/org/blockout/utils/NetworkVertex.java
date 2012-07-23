package org.blockout.utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class NetworkVertex implements Comparable<NetworkVertex> {
	private String				id;
	private final List<String>	connections;

	public NetworkVertex() {
		connections = new LinkedList<String>();
		id = "";
	}

	public NetworkVertex(final String id) {
		this();
		this.id = id;
	}

	public void setId( final String id ) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void addConnection( final String address ) {
		connections.add( address );
	}

	public void removeConnection( final String address ) {
		connections.remove( address );
	}

	public List<String> getConnections() {
		return Collections.unmodifiableList( connections );
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((connections == null) ? 0 : connections.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals( final Object obj ) {
		if ( this == obj ) {
			return true;
		}

		if ( obj == null ) {
			return false;
		}

		if ( getClass() != obj.getClass() ) {
			return false;
		}

		NetworkVertex other = (NetworkVertex) obj;

		if ( id == null ) {
			if ( other.id != null ) {
				return false;
			}
		} else if ( !id.equals( other.id ) ) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public int compareTo( final NetworkVertex o ) {
		return id.compareTo( o.id );
	}
}
