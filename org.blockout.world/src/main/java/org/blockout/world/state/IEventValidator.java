package org.blockout.world.state;

import org.blockout.common.IEvent;

/**
 * Implement this interface to take part in the event validation process of a
 * {@link IStateMachine}.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public interface IEventValidator {

	/**
	 * Gets invoked when the {@link IStateMachine} needs to validate if a given
	 * event is valid in context of global game state or not.
	 * 
	 * @param event
	 *            The event to validate.
	 * @return The result of this validation. Return
	 *         {@link ValidationResult#Unknown} if the implementation can't rate
	 *         the given event.
	 */
	public ValidationResult validateEvent( IEvent<?> event );
}
