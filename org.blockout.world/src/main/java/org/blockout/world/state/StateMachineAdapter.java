package org.blockout.world.state;

import org.blockout.common.IEvent;

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
	public void performEvent( final IEvent<?> event ) {
	}

	@Override
	public void undoEvent( final IEvent<?> event ) {
	}

}
