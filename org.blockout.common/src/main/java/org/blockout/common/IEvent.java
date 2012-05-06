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
public interface IEvent<T extends IEvent<T>> {
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

	public boolean isInverseOf( IEvent<T> event );

	/**
	 * Returns the inverse of this event. This method might return different
	 * event instances with an unique UUID on each invocation. To check whether
	 * a given event is the inverse of another use {@link #isInverseOf(IEvent)}.
	 * 
	 * Not really meaningful for AttackEvents, etc.
	 * 
	 * @return
	 */
	public T getInverse();

	/**
	 * Returns whether the given object has the same data type and UUID as this
	 * instance.
	 * 
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals( Object obj );

	/**
	 * Returns the hash code of this event. Implementations commonly use the
	 * hash code of the UUID of this event.
	 * 
	 * @return
	 */
	@Override
	public int hashCode();
}
