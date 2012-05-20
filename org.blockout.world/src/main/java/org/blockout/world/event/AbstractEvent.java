package org.blockout.world.event;

import java.util.Calendar;
import java.util.UUID;

/**
 * Base class for events.
 * 
 * @author Marc-Christian Schulze
 * 
 * @param <T>
 *            The concrete sub-class of the event.
 */
public abstract class AbstractEvent<T extends IEvent<T>> implements IEvent<T> {

	private static final long	serialVersionUID	= -6451235233914239534L;
	protected Calendar			localTime;
	protected UUID				id;

	/**
	 * Constructs a new event with a unique id and the current local time.
	 */
	public AbstractEvent() {
		id = UUID.randomUUID();
		localTime = Calendar.getInstance();
	}

	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public Calendar getLocalTime() {
		return localTime;
	}
}