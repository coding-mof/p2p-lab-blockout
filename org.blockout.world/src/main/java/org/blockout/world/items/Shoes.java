package org.blockout.world.items;

import org.blockout.engine.SpriteType;

import com.google.common.base.Preconditions;

/**
 * Instances of this class represent shoes that the player can wear.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class Shoes implements Item {

	private static final long	serialVersionUID	= -2891411239783000176L;
	protected int				protection;

	public Shoes(final int protection) {
		Preconditions.checkArgument( protection >= 0, "Protection must be greater or equal to zero." );
		this.protection = protection;
	}

	public int getProtection() {
		return protection;
	}

	@Override
	public String getName() {
		return "Shoes";
	}

	@Override
	public String toString() {
		return "Shoes[protection=" + protection + "]";
	}

	@Override
	public SpriteType getSpriteType() {
		return SpriteType.hell_hound_pup;
	}
}
