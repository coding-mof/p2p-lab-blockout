package org.blockout.engine;

import com.google.common.base.Preconditions;

/**
 * Mapping between sprite types and sprite ids used by the {@link ISpriteSheet}.
 * 
 * @author Marc-Christian Schulze
 * @author Florian Müller
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
			case Player:
				return 245;

				// World
			case StoneGround:
				return 848;
			case Crate:
				return 586;

				// Monsters
			case Zombie:
				return 249;
			case Skeleton:
				return 250;

			default:
				throw new IllegalArgumentException( "Unknown sprite type: " + type );
		}
	}

	public SpriteType getSpriteType( final int id ) {

		switch ( id ) {
			case 245:
				return SpriteType.Player;

				// World
			case 848:
				return SpriteType.StoneGround;
			case 586:
				return SpriteType.Crate;

				// Monsters
			case 249:
				return SpriteType.Zombie;
			case 250:
				return SpriteType.Skeleton;

			default:
				throw new IllegalArgumentException( "Unknown sprite id: " + id );
		}
	}
}
