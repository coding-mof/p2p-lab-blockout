package org.blockout.world.entity;

import org.blockout.engine.SpriteType;

import com.google.common.base.Preconditions;

/**
 * Common base class for all kinds of monsters.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class Monster extends Actor {

	private static final long	serialVersionUID	= 8049484327263845076L;
	protected int				strength;
	protected int				armor;

	public Monster(final String name, final int level, final int strength, final int armor) {
		super( name );
		setLevel( level );
		setCurrentHealth( getMaxHealth() );
		setStrength( strength );
		setArmor( armor );
	}

	@Override
	public int getStrength() {
		return strength;
	}

	public void setStrength( final int strength ) {
		Preconditions.checkArgument( strength > 0, "Strength must be greater than zero." );
		this.strength = strength;
	}

	@Override
	public int getArmor() {
		return armor;
	}

	public void setArmor( final int armor ) {
		Preconditions.checkArgument( armor >= 0, "Armor must be greater or equal to zero." );
		this.armor = armor;
	}

	@Override
	public SpriteType getSpriteType() {

		return SpriteType.Zombie;
	}
}
