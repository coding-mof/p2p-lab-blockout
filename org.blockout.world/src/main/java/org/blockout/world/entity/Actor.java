package org.blockout.world.entity;

import org.newdawn.slick.util.pathfinding.Mover;

import com.google.common.base.Preconditions;

/**
 * Abstract base class for all active game objects that are controlled either by
 * a player or an AI.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public abstract class Actor implements Entity, Mover {

	private static final long	serialVersionUID	= -5097030020273809455L;
	protected String			name;
	protected int				level;
	protected int				currentHealth;

	protected Actor(final String name) {
		setName( name );
		setLevel( 1 );
		setCurrentHealth( getMaxHealth() );
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the player.
	 * 
	 * @param name
	 *            The name of the player.
	 * @throws NullPointerException
	 *             If you pass in null.
	 */
	public void setName( final String name ) {
		Preconditions.checkNotNull( name );
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	/**
	 * Sets the level of the player.
	 * 
	 * @param level
	 * @throws IllegalArgumentException
	 *             If you pass in zero or a negative number.
	 */
	public void setLevel( final int level ) {
		Preconditions.checkArgument( level > 0, "Player's level can only be a positive number greater than 0. Got %d",
				level );
		this.level = level;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}

	/**
	 * Sets the current health of the player.
	 * 
	 * @param currentHealth
	 * @throws IllegalArgumentException
	 *             If you pass in zero or a negative number.
	 */
	public void setCurrentHealth( final int currentHealth ) {
		Preconditions.checkArgument( currentHealth > 0, "Player's health can only be greater than zero. Got %d.",
				currentHealth );
		Preconditions.checkArgument( currentHealth <= getMaxHealth(),
				"Player's health can't be greater than the maximum." );
		this.currentHealth = currentHealth;
	}

	public int getMaxHealth() {
		return 100 + (getLevel() * 50);
	}

	public abstract int getStrength();

	public abstract int getArmor();
}