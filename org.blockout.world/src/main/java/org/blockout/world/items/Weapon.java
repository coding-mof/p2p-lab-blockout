package org.blockout.world.items;

import com.google.common.base.Preconditions;

/**
 * Instances of this class represent weapons the player can use.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class Weapon implements Item {

	private static final long	serialVersionUID	= -542381053531355902L;
	protected int				strength;

	public Weapon(final int strength) {
		Preconditions.checkArgument( strength >= 0, "Strength must be greater or equal to zero." );
		this.strength = strength;
	}

	public int getStrength() {
		return strength;
	}

	@Override
	public String getName() {
		return "Weapon";
	}

	@Override
	public String toString() {
		return "Weapon[strength=" + strength + "]";
	}
}
