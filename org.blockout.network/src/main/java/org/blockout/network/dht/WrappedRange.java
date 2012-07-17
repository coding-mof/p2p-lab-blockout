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
				return upper < 0 || (lower > 0 || lower == 0);
			} else {
				return lower < 0 && (upper > 0 || upper == 0);
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
		return "( " + this.lowerBound + " , " + this.upperBound + " ] WA: " + this.wrapAround;
	}
}
