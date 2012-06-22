package org.blockout.ai;

/**
 * Common interface for AI implementations. Implementations have to perform
 * their operations step wise.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public interface AIPlayer {

	/**
	 * Gets invoked when the player has to play his next step like in a RPG.
	 * This method has to return quickly and is not allowed to block.
	 */
	public abstract void doNextStep();

}