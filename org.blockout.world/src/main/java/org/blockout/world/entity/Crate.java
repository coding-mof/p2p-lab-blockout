package org.blockout.world.entity;

import org.blockout.engine.SpriteType;

public class Crate implements Entity {

	@Override
	public String getName() {
		return "Crate";
	}

	@Override
	public SpriteType getSpriteType() {
		return SpriteType.Crate;
	}

}
