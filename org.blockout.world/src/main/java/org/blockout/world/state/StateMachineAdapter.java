package org.blockout.world.state;

import org.blockout.world.event.IEvent;

/**
 * Adapter for convenience use of the {@link IStateMachineListener} interface.
 * Extend this class if you are only interested in a subset of the raised events
 * and override the desired methods.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class StateMachineAdapter implements IStateMachineListener {

	@Override
	public void eventCommitted( final IEvent<?> event ) {
	}

	@Override
	public void eventPushed( final IEvent<?> event ) {
	}

	@Override
	public void eventRolledBack( final IEvent<?> event ) {
	}

	@Override
	public void init( final IStateMachine stateMachine ) {
	}
}
