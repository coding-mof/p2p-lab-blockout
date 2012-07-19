package org.blockout.world.entity;

import java.util.Random;
import java.util.UUID;

import org.blockout.engine.SpriteType;
import org.blockout.world.items.Armor;
import org.blockout.world.items.Elixir;
import org.blockout.world.items.Elixir.Type;
import org.blockout.world.items.Helm;
import org.blockout.world.items.Item;
import org.blockout.world.items.Shield;
import org.blockout.world.items.Shoes;
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

		Random rand = new Random( System.currentTimeMillis() );
		switch ( rand.nextInt( 6 ) ) {
			case 0:
				item = new Weapon( rand.nextInt( 40 ) + 1 );
				break;
			case 1:
				item = new Helm( rand.nextInt( 20 ) + 1 );
				break;
			case 2:
				item = new Shield( rand.nextInt( 30 ) + 1 );
				break;
			case 3:
				item = new Shoes( rand.nextInt( 10 ) + 1 );
				break;
			case 4:
				item = new Armor( rand.nextInt( 50 ) + 1 );
				break;
			case 5:
				item = new Elixir( Type.Health, rand.nextInt( 50 ) + 10 );
				break;
			default:
				item = null;
		}
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

	@Override
	public String toString() {
		return "Crate[item=" + getItem() + ", id=" + getId() + "]";
	}
}
