package org.blockout.world.items;

import com.google.common.base.Preconditions;

/**
 * Instances of this class represent gloves that the player can wear.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class Gloves implements Item {

	private static final long	serialVersionUID	= -5516595822116967578L;
	protected int				protection;

	public Gloves(final int protection) {
		Preconditions.checkArgument( protection >= 0, "Protection must be greater or equal to zero." );
		this.protection = protection;
	}

	public int getProtection() {
		return protection;
	}

	@Override
	public String getName() {
		return "Gloves";
	}

	@Override
	public String toString() {
		return "Gloves[protection=" + protection + "]";
	}
}
