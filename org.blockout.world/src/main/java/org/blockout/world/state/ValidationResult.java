package org.blockout.world.state;

/**
 * Values of this enumeration represent the result returned by an
 * {@link IEventValidator} after he has validated an event.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public enum ValidationResult {
	/**
	 * The {@link IEventValidator} can't validate the given event type.
	 */
	Unknown,
	/**
	 * The event is valid in the context of the {@link IEventValidator}.
	 */
	Valid,
	/**
	 * The event is invalid in the context of the {@link IEventValidator}.
	 */
	Invalid
}
