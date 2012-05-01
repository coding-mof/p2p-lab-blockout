package org.blockout.common;

import java.util.Calendar;
import java.util.UUID;

/**
 * Common interface for all game logic events like player movements, attacks,
 * etc.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public interface IEvent {
	/**
	 * Returns the unique id of this event.
	 * 
	 * @return
	 */
	public UUID getId();

	/**
	 * Returns the local time when the event has occurred.
	 * 
	 * @return
	 */
	public Calendar getLocalTime();

	/**
	 * Returns the inverse of this event.
	 * 
	 * @return
	 */
	public IEvent getInverse();
}
