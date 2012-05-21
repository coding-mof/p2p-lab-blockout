package org.blockout.world.entity;

import java.io.Serializable;

import org.blockout.engine.SpriteType;

/**
 * Common interface for all game entities like Monsters, Players, Crates, etc.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public interface Entity extends Serializable {

	public String getName();

	public SpriteType getSpriteType();
}
