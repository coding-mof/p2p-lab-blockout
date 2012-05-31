package org.blockout.network.dht;

import java.io.Serializable;

public class HashRange<C extends Comparable> implements Serializable {
	private static final long serialVersionUID = 0;
	private C lowerBound;
	private C upperBound;
	private boolean wrapAround;

	public HashRange() {
		this.lowerBound = null;
		this.upperBound = null;
		this.wrapAround = false;
	}

	public HashRange(C lowerBound, C upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.wrapAround = this.lowerBound.compareTo(this.upperBound) > 0;
	}

	public boolean contains(C element) {
		if (this.lowerBound == null && this.upperBound == null) {
			return true;
		} else {
			int lower = this.lowerBound.compareTo(element);
			int upper = this.upperBound.compareTo(element);
			if (this.wrapAround) {
				return upper < 0 || (lower > 0 || lower == 0);
			} else {
				return lower < 0 && (upper > 0 || upper == 0);
			}
		}
	}

	@Override
	public String toString() {
		return "( " + this.lowerBound + " , " + this.upperBound + " ] WA: "
				+ this.wrapAround;
	}
}
