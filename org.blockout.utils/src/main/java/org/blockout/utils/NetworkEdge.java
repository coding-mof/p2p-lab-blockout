package org.blockout.utils;

public class NetworkEdge implements Comparable<NetworkEdge> {

	private static long		next_index	= 0;

	private final long		index;
	private final String	type;
	private final String	label;

	public NetworkEdge(final String type, final String label) {
		this.type = type;
		this.label = label;
		index = next_index++;
	}

	public String getType() {
		return type;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (index ^ (index >>> 32));
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

		NetworkEdge other = (NetworkEdge) obj;

		if ( index != other.index ) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return label;
	}

	@Override
	public int compareTo( final NetworkEdge o ) {
		int diff = (int) (index - o.index);
		if ( diff != 0 ) {
			return diff;
		}
		diff = type.compareTo( o.type );
		if ( diff != 0 ) {
			return diff;
		}
		diff = label.compareTo( o.label );
		if ( diff != 0 ) {
			return diff;
		}
		return 0;
	}
}
