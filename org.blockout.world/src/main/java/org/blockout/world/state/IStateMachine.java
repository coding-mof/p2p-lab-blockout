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

	/**
	 * Pushes an event into the state machine. The state machine has to discard
	 * duplicates. All other events are passed to the validators which elect if
	 * the given event is valid in the context of the local game state. If not
	 * {@link ValidationResult#Invalid} is returned. Otherwise the event gets
	 * stored and the listener get notified by
	 * {@link IStateMachineListener#eventPushed(IEvent)}.
	 * 
	 * 
	 * @param event
	 * @return
	 */
	public ValidationResult pushEvent( IEvent<?> event );

	/**
	 * Commit a previously pushed event. If the event was not pushed or has been
	 * rolled back in the past, this method acts as an auto-commit which raises
	 * first an {@link IStateMachineListener#eventPushed(IEvent)} and then an
	 * {@link IStateMachineListener#eventCommitted(IEvent)} event.
	 * 
	 * @param event
	 */
	public void commitEvent( IEvent<?> event );

	/**
	 * Rolls a pushed event back. This method can only roll back uncommitted
	 * events. Furthermore it ignores rollback events which have never been
	 * pushed before.
	 * 
	 * @param event
	 */
	public void rollbackEvent( IEvent<?> event );
}