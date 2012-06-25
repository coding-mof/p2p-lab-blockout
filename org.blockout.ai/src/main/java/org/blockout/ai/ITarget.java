package org.blockout.ai;

/**
 * Common interface for all temporary targets of an {@link AIPlayer}. Implement
 * this interface to define custom behaviour of an {@link AIPlayer}.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public interface ITarget {
	/**
	 * Returns the priority of the target. Higher priorities preempt targets
	 * with lower priority when detected.
	 * 
	 * @return
	 */
	public int getPriority();

	/**
	 * Perform one step to get closer to your target.
	 */
	public void approach();

	/**
	 * Returns whether you reached your target.
	 * 
	 * @return
	 */
	public boolean achieved();
}
