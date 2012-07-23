package org.blockout.utils;

import com.google.common.base.Preconditions;

/**
 * Class to represent an vertex in the network graph
 * 
 * @author Florian Müller
 */
public class NetworkVertex implements Comparable<NetworkVertex> {
	private String				id;

    /**
     * Construct to create a new vertex with a unique id
     * 
     * @param id
     *            Unique id of this vertex
     * 
     * @throws NullPointerException
     *             Thrown if an argument is null
     */
	public NetworkVertex(final String id) {
        Preconditions.checkNotNull( id, "id is null" );

		this.id = id;
	}

    /**
     * Returns the id if this vertex
     * 
     * @return The id of this vertex as a String
     */
	public String getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
