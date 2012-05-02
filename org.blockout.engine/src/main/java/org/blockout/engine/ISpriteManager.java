package org.blockout.engine;

import org.newdawn.slick.Image;

/**
 * Implementations have to provide access to the sprites of the game.
 * Implementations are free to cache the sprites.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public interface ISpriteManager {

	/**
	 * Returns the sprite of the given type. The returned sprite might be cached
	 * by this implementation.
	 * 
	 * @param type
	 *            The type of the sprite.
	 * @return The sprite identified by the type or null if sprite is unknown.
	 */
	public Image getSprite( SpriteType type );
	
	/**
	 * Returns the sprite of the given type and optionally removes the background. 
	 * The returned sprite might be cached by this implementation.
	 * 
	 * @param type
	 *            The type of the sprite.
	 * @param excludeBackground
	 * 			  True if the background should be removed from the sprite, false otherwise.
	 * @return The sprite identified by the type or null if sprite is unknown.
	 */
	public Image getSprite( SpriteType type, boolean excludeBackground);
}
