package org.blockout.logic.handler;

import org.blockout.world.event.IEvent;
import org.blockout.world.state.IStateMachine;

public interface IEventHandler {
	public void eventStarted( IStateMachine stateMachine, IEvent<?> event );

	public void eventFinished( IStateMachine stateMachine, IEvent<?> event );
}
