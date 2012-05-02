package org.blockout.world.state;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.blockout.common.IEvent;

import com.google.common.base.Preconditions;

/**
 * Default implementation of the state machine.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class DefaultStateMachine extends AbstractStateMachine {

	protected final Object			lock	= new Object();
	protected Map<UUID, IEvent<?>>	events;

	public DefaultStateMachine() {
		events = new HashMap<UUID, IEvent<?>>();
	}

	@Override
	public ValidationResult pushEvent( final IEvent<?> event ) {
		Preconditions.checkNotNull( event );
		synchronized ( lock ) {
			if ( events.containsKey( event.getId() ) ) {
				return ValidationResult.Invalid;
			}
		}

		ValidationResult result = validateEvent( event );
		if ( result == ValidationResult.Invalid ) {
			return result;
		} else {
			synchronized ( lock ) {
				events.put( event.getId(), event );
			}
			firePerformEvent( event );
		}
		return ValidationResult.Valid;
	}

	@Override
	public void commitEvent( final IEvent<?> event ) {
		Preconditions.checkNotNull( event );
		synchronized ( lock ) {
			if ( !events.containsKey( event.getId() ) ) {
				firePerformEvent( event );
			} else {
				events.remove( event.getId() );
			}
		}
		fireEventCommitted( event );
	}

	@Override
	public void denyEvent( final IEvent<?> event ) {
		Preconditions.checkNotNull( event );
		synchronized ( lock ) {
			if ( events.containsKey( event.getId() ) ) {
				events.remove( event.getId() );
				fireUndoEvent( event.getInverse() );
			}
		}
	}

}
