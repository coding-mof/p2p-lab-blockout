package org.blockout.world.entity;

import java.io.Serializable;
import java.util.UUID;

import org.blockout.engine.SpriteType;

/**
 * Common interface for all game entities like Monsters, Players, Crates, etc.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public interface Entity extends Serializable {

	public UUID getId();

	public String getName();

	public SpriteType getSpriteType();
}
