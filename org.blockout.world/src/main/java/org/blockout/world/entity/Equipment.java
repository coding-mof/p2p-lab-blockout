package org.blockout.world.entity;

import java.io.Serializable;

import org.blockout.world.items.Armor;
import org.blockout.world.items.Elixir;
import org.blockout.world.items.Gloves;
import org.blockout.world.items.Helm;
import org.blockout.world.items.Shield;
import org.blockout.world.items.Shoes;
import org.blockout.world.items.Weapon;

import com.google.common.base.Preconditions;

/**
 * This class models the equipment of a player. The equipment contains the items
 * that the player currently wears and uses passivly.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class Equipment implements Serializable {

	private static final long	serialVersionUID	= 6565731945095101258L;
	protected Helm				helm;
	protected Armor				armor;
	protected Weapon			weapon;
	protected Shield			shield;
	protected Gloves			gloves;
	protected Shoes				shoes;

	protected Elixir[]			belt;

	public Equipment() {
		belt = new Elixir[6];
	}

	/**
	 * Returns the elixir at position <code>index</code>.
	 * 
	 * @param index
	 *            The position. Must be in range of [0,6).
	 * @return The elixir at position or null if no present.
	 */
	public Elixir getBeltItem( final int index ) {
		Preconditions.checkArgument( index >= 0 && index < 6, "Index must be in the range of [0,6)." );
		return belt[index];
	}

	public int getBeltSize() {
		return 6;
	}

	public boolean isBeltFull() {
		for ( int i = 0; i < getBeltSize(); i++ ) {
			if ( belt[i] == null ) {
				return false;
			}
		}
		return true;
	}

	public boolean putInBelt( final Elixir elixir ) {
		for ( int i = 0; i < getBeltSize(); i++ ) {
			if ( belt[i] == null ) {
				belt[i] = elixir;
				return true;
			}
		}
		return false;
	}

	public void setBeltItem( final int index, final Elixir e ) {
		belt[index] = e;
	}

	/**
	 * Computes the protection given by the items in the equipment.
	 * 
	 * @return The protection given by the items in the equipment.
	 */
	public int computeProtection() {
		int protection = 0;
		if ( helm != null ) {
			protection += helm.getProtection();
		}
		if ( armor != null ) {
			protection += armor.getProtection();
		}
		if ( shield != null ) {
			protection += shield.getProtection();
		}
		if ( gloves != null ) {
			protection += gloves.getProtection();
		}
		if ( shoes != null ) {
			protection += shoes.getProtection();
		}
		return protection;
	}

	/**
	 * Computes the strength given by the items in the equipment.
	 * 
	 * @return The strength given by the items in the equipment.
	 */
	public int computeStrength() {
		int strength = 0;
		if ( weapon != null ) {
			strength += weapon.getStrength();
		}
		return strength;
	}

	public Helm getHelm() {
		return helm;
	}

	public void setHelm( final Helm helm ) {
		this.helm = helm;
	}

	public Armor getArmor() {
		return armor;
	}

	public void setArmor( final Armor armor ) {
		this.armor = armor;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon( final Weapon weapon ) {
		this.weapon = weapon;
	}

	public Shield getShield() {
		return shield;
	}

	public void setShield( final Shield shield ) {
		this.shield = shield;
	}

	public Gloves getGloves() {
		return gloves;
	}

	public void setGloves( final Gloves gloves ) {
		this.gloves = gloves;
	}

	public Shoes getShoes() {
		return shoes;
	}

	public void setShoes( final Shoes shoes ) {
		this.shoes = shoes;
	}
}
