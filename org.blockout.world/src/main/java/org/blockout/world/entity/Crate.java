package org.blockout.world.entity;

import org.blockout.engine.SpriteType;

/**
 * Instances of this class represent {@link Crate}s that the {@link Player} can
 * open.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class Crate implements Entity {

	private static final long	serialVersionUID	= -5301210134052727017L;

	@Override
	public String getName() {
		return "Crate";
	}

	@Override
	public SpriteType getSpriteType() {
		return SpriteType.Crate;
	}

}
