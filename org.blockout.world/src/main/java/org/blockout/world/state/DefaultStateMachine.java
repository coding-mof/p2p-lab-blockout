package org.blockout.world.state;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.blockout.world.event.IEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

import com.google.common.base.Preconditions;

/**
 * Default implementation of the state machine.
 * 
 * @author Marc-Christian Schulze
 * @threadSafety unconditionally thread-safe
 */
public class DefaultStateMachine extends AbstractStateMachine {

	private static final Logger				logger;
	static {
		logger = LoggerFactory.getLogger( DefaultStateMachine.class );
	}
	protected final Map<UUID, IEvent<?>>	events;

	public DefaultStateMachine(final TaskExecutor executor) {
		super( executor );
		events = new HashMap<UUID, IEvent<?>>();
	}

	@Override
	public ValidationResult pushEvent( final IEvent<?> event ) {
		Preconditions.checkNotNull( event );
		synchronized ( events ) {
			if ( events.containsKey( event.getId() ) ) {
				return ValidationResult.Invalid;
			}
		}

		ValidationResult result = validateEvent( event );
		if ( result == ValidationResult.Invalid ) {
			return result;
		} else {
			synchronized ( events ) {
				events.put( event.getId(), event );
			}
			fireEventPushed( event );
		}
		return ValidationResult.Valid;
	}

	@Override
	public void commitEvent( final IEvent<?> event ) {
		Preconditions.checkNotNull( event );
		synchronized ( events ) {
			if ( !events.containsKey( event.getId() ) ) {
				logger.warn( "Received commit for inactive event. Either we got no push event or the event has been rolled back. Discarding "
						+ event );
			} else {
				events.remove( event.getId() );
			}
		}
		fireEventCommitted( event );
	}

	@Override
	public void rollbackEvent( final IEvent<?> event ) {
		Preconditions.checkNotNull( event );
		synchronized ( events ) {
			if ( events.containsKey( event.getId() ) ) {
				events.remove( event.getId() );
				fireEventRolledBack( event );
			}
		}
	}
}
