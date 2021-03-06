package org.blockout.world.state;

import org.blockout.world.event.IEvent;

/**
 * Implement this interface to receive events of the internal event processing
 * of a state machine.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public interface IStateMachineListener {

	public void init( IStateMachine stateMachine );

	/**
	 * An event has been committed by the responsible server node. Committed
	 * events can't be undone.
	 * 
	 * @param event
	 */
	public void eventCommitted( IEvent<?> event );

	/**
	 * A new event has been pushed into the state machine and no local
	 * {@link IEventValidator} revoked it.
	 * 
	 * @param event
	 */
	public void eventPushed( IEvent<?> event );

	/**
	 * This event has either been revoked by another peer or a server node. If
	 * this event has been wrongly revoked by a peer you can assume that the
	 * responsible server node will re-inject the correct event and the
	 * {@link #eventCommitted(IEvent)} gets invoked. Therefore it's safe to undo
	 * the given event.
	 * 
	 * @param event
	 *            The inverse of the event to undo. Just apply the event - don't
	 *            compute the inverse again.
	 */
	public void eventRolledBack( IEvent<?> event );
}
