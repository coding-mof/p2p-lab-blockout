package org.blockout.logic;

import com.google.common.base.Preconditions;

/**
 * Instances of this class represent a human player.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class Player {
	protected String	name;
	protected int		experience;
	protected int		level;
	protected int		currentHealth;

	/**
	 * Constructs a new player with the given name, full health, no experience
	 * at level 1.
	 * 
	 * @param name
	 *            The name of the player
	 * @throws NullPointerException
	 *             If you pass in null as name.
	 */
	public Player(final String name) {
		setName( name );
		setLevel( 1 );
		setCurrentHealth( getMaxHealth() );
	}

	@SuppressWarnings("unused")
	private Player() {
		// just here to let jackson deserialize the object
	}

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

	public int getExperience() {
		return experience;
	}

	/**
	 * Sets the experience of the player.
	 * 
	 * @param experience
	 * @throws IllegalArgumentException
	 *             If you pass in a negative number.
	 */
	public void setExperience( final int experience ) {
		Preconditions.checkArgument( experience >= 0,
				"Player's experience can only be greater or equal to zero. Got %d.", experience );
		this.experience = experience;
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
}
