package org.blockout.world.entity;

import org.blockout.engine.SpriteType;

import com.google.common.base.Preconditions;

/**
 * Instances of this class represent a human player.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class Player extends Actor {
	protected int		experience;
	protected Inventory	inventory;

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
		super( name );
		inventory = new Inventory();
	}

	private Player() {
		super( "" );
		// just here to let jackson deserialize the object
	}

	public Inventory getInventory() {
		return inventory;
	}

	public int getExperience() {
		return experience;
	}

	public int getRequiredExperience() {
		return 100 * getLevel();
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
				"Player's experience can only be greater or equal to zero. Got %s.", experience );
		this.experience = experience;
	}

	@Override
	public SpriteType getSpriteType() {
		return SpriteType.Player;
	}

	@Override
	public int getStrength() {
		return 20 + getLevel() * 15;
	}

	@Override
	public int getArmor() {
		return 30 + getLevel() * 10;
	}

	@Override
	public String toString() {
		return "Player[name='" + getName() + "']";
	}
}
