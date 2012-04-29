package org.blockout.engine;

import com.google.common.base.Preconditions;

/**
 * Mapping between sprite types and sprite ids used by the {@link ISpriteSheet}.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class SpriteMapping {

	/**
	 * Returns the id of a sprite type.
	 * 
	 * @param type
	 *            The type of the sprite.
	 * @return The id of the sprite.
	 * @throws IllegalArgumentException
	 *             If you pass in an unknown sprite type.
	 * @throws NullPointerException
	 *             If you pass in null.
	 */
	public int getSpriteId( final SpriteType type ) {
		Preconditions.checkNotNull( type );
		switch ( type ) {

			default:
				throw new IllegalArgumentException( "Unknown sprite type: " + type );
		}
	}
}
