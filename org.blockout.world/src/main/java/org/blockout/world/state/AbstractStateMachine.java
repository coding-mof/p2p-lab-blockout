package org.blockout.world.state;

import java.util.ArrayList;
import java.util.List;

import org.blockout.common.IEvent;

/**
 * Abstract base class for all state machine implementations which provides
 * listener management support.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public abstract class AbstractStateMachine implements IStateMachine {

	protected List<IEventValidator>			validators;
	protected List<IStateMachineListener>	listener;

	protected AbstractStateMachine() {
		validators = new ArrayList<IEventValidator>();
		listener = new ArrayList<IStateMachineListener>();
	}

	@Override
	public void addEventValidator( final IEventValidator validator ) {
		validators.add( validator );
	}

	@Override
	public void removeEventValidator( final IEventValidator validator ) {
		validators.remove( validator );
	}

	@Override
	public void addIStateMachineListener( final IStateMachineListener l ) {
		listener.add( l );
	}

	@Override
	public void removeIStateMachineListener( final IStateMachineListener l ) {
		listener.remove( l );
	}

	protected ValidationResult validateEvent( final IEvent event ) {
		for ( IEventValidator validator : validators ) {
			if ( validator.validateEvent( event ) == ValidationResult.Invalid ) {
				return ValidationResult.Invalid;
			}
		}
		return ValidationResult.Valid;
	}

	protected void fireEventCommitted( final IEvent event ) {
		for ( IStateMachineListener l : listener ) {
			l.eventCommitted( event );
		}
	}

	protected void firePerformEvent( final IEvent event ) {
		for ( IStateMachineListener l : listener ) {
			l.performEvent( event );
		}
	}

	protected void fireUndoEvent( final IEvent event ) {
		for ( IStateMachineListener l : listener ) {
			l.undoEvent( event );
		}
	}
}
