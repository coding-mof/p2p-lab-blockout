package org.blockout.network.dht;

import java.io.Serializable;

public class WrappedRange<C extends Comparable<C>> implements Serializable {
	private static final long	serialVersionUID	= 0;
	private final C				lowerBound;
	private final C				upperBound;
	private final boolean		wrapAround;

	public WrappedRange() {
		this.lowerBound = null;
		this.upperBound = null;
		this.wrapAround = false;
	}

	public WrappedRange(final C lowerBound, final C upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.wrapAround = this.lowerBound.compareTo( this.upperBound ) > 0;
	}

	public boolean contains( final C element ) {
		if ( this.lowerBound == null && this.upperBound == null ) {
			return true;
		} else {
			int lower = this.lowerBound.compareTo( element );
			int upper = this.upperBound.compareTo( element );
			if ( this.wrapAround ) {
				return upper >= 0 || lower <= 0;
			} else {
				return lower <= 0 && upper >= 0;
			}
		}
	}

	public C getLowerBound() {
		return lowerBound;
	}

	public C getUpperBound() {
		return upperBound;
	}

	@Override
	public String toString() {
		return "[" + this.lowerBound + ", " + this.upperBound + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lowerBound == null) ? 0 : lowerBound.hashCode());
		result = prime * result + ((upperBound == null) ? 0 : upperBound.hashCode());
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
		WrappedRange<?> other = (WrappedRange<?>) obj;
		if ( lowerBound == null ) {
			if ( other.lowerBound != null ) {
				return false;
			}
		} else if ( !lowerBound.equals( other.lowerBound ) ) {
			return false;
		}
		if ( upperBound == null ) {
			if ( other.upperBound != null ) {
				return false;
			}
		} else if ( !upperBound.equals( other.upperBound ) ) {
			return false;
		}
		return true;
	}
}
