package org.blockout.world;

import org.blockout.common.IEvent;

/**
 * Interface for the core component of the event system. A state machine can
 * have multiple {@link IEventValidator}s which are asked to validate a given
 * event when it gets put into the state machine and is not a duplicate. The
 * state machine asks each {@link IEventValidator} as long the respond with
 * "valid".
 * 
 * @author Marc-Christian Schulze
 * 
 */
public interface IStateMachine {
	/**
	 * 
	 * @param validator
	 */
	public void addEventValidator( IEventValidator validator );

	public void removeEventValidator( IEventValidator validator );

	public void pushEvent( IEvent event );

}