package org.blockout.world.items;

import com.google.common.base.Preconditions;

/**
 * Instances of this clas respresent a armor that a player can wear.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class Armor implements Item {
	protected int	protection;

	public Armor(final int protection) {
		Preconditions.checkArgument( protection >= 0, "Protection must be greater or equal to zero." );
		this.protection = protection;
	}

	public int getProtection() {
		return protection;
	}

	@Override
	public String getName() {
		return "Armor";
	}

	@Override
	public String toString() {
		return "Armor[protection=" + protection + "]";
	}
}
