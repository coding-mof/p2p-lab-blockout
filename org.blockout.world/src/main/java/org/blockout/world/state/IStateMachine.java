package org.blockout.world.state;

import org.blockout.common.IEvent;

/**
 * Interface for the core component of the event system. A state machine can
 * have multiple {@link IEventValidator}s which are asked to validate a given
 * event when it gets put into the state machine and is not a duplicate one. The
 * state machine asks each {@link IEventValidator} as long as they respond with
 * {@link ValidationResult#Valid} or {@link ValidationResult#Unknown}.
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

	public void addIStateMachineListener( IStateMachineListener l );

	public void removeIStateMachineListener( IStateMachineListener l );

	public ValidationResult pushEvent( IEvent<?> event );

	public void commitEvent( IEvent<?> event );

	public void denyEvent( IEvent<?> event );
}