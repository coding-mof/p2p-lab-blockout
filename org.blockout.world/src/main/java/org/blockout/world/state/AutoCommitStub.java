package org.blockout.world.state;

import javax.inject.Named;

import org.blockout.common.IEvent;

@Named
public class AutoCommitStub implements IStateMachineListener {

	private IStateMachine	stateMachine;

	@Override
	public void eventCommitted( final IEvent<?> event ) {
	}

	@Override
	public void eventPushed( final IEvent<?> event ) {
		stateMachine.commitEvent( event );
	}

	@Override
	public void eventRolledBack( final IEvent<?> event ) {
	}

	@Override
	public void init( final IStateMachine stateMachine ) {
		this.stateMachine = stateMachine;
	}

}
