package org.blockout.world.entity;

import java.util.UUID;

import org.blockout.engine.SpriteType;
import org.blockout.world.items.Item;
import org.blockout.world.items.Weapon;

/**
 * Instances of this class represent {@link Crate}s that the {@link Player} can
 * open.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class Crate implements Entity {

	private static final long	serialVersionUID	= -5301210134052727017L;

	protected UUID				id;
	// Currently we have only a single item in a crate
	protected Item				item;

	public Crate() {
		id = UUID.randomUUID();
		item = new Weapon( 10 );
	}

	public Item removeItem() {
		try {
			return item;
		} finally {
			item = null;
		}
	}

	public Item getItem() {
		return item;
	}

	@Override
	public String getName() {
		return "Crate";
	}

	@Override
	public SpriteType getSpriteType() {
		return SpriteType.Crate;
	}

	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals( final Object obj ) {
		if ( this == obj ) {
			return true;
		}
		if ( obj == null ) {
			return false;
		}
		if ( getClass() != obj.getClass() ) {
			return false;
		}
		Crate other = (Crate) obj;
		if ( id == null ) {
			if ( other.id != null ) {
				return false;
			}
		} else if ( !id.equals( other.id ) ) {
			return false;
		}
		return true;
	}

}
