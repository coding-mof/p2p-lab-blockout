package org.blockout.world.event;

import java.util.Calendar;
import java.util.UUID;

import org.blockout.common.TileCoordinate;

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

	/**
	 * Returns how long it takes until the event is finished. The duration is
	 * measured in milliseconds.
	 * 
	 * @return The duration of this event in milliseconds or 0 if no duration
	 *         configured.
	 */
	public long getDuration();

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
	 * Returns the tile that is responsible for managing this event. This tile
	 * is used in the network adapter to distinguish between events that have to
	 * be committed by us and those where other peers are responsible for.
	 * 
	 * @return
	 */
	public TileCoordinate getResponsibleTile();

	/**
	 * Returns the hash code of this event. Implementations commonly use the
	 * hash code of the UUID of this event.
	 * 
	 * @return
	 */
	@Override
	public int hashCode();
}
