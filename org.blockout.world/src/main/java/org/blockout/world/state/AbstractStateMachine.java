package org.blockout.world.state;


/**
 * The state machine is the core component of the event handling. Local and
 * remote events are passed to it.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public abstract class AbstractStateMachine implements IStateMachine {

	@Override
	public void addEventValidator( final IEventValidator validator ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeEventValidator( final IEventValidator validator ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addIStateMachineListener( final IStateMachineListener l ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeIStateMachineListener( final IStateMachineListener l ) {
		// TODO Auto-generated method stub

	}
}
