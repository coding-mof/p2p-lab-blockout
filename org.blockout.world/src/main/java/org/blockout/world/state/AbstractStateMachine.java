package org.blockout.world.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.blockout.world.event.IEvent;
import org.springframework.core.task.TaskExecutor;

import com.google.common.base.Preconditions;

/**
 * Abstract base class for all state machine implementations which provides
 * listener management support.
 * 
 * @author Marc-Christian Schulze
 * @threadSafety unconditionally thread-safe
 */
public abstract class AbstractStateMachine implements IStateMachine {

	protected final List<IEventValidator>	validators;
	protected List<IStateMachineListener>	listener;
	protected TaskExecutor					threadPool;

	protected AbstractStateMachine(final TaskExecutor executor) {
		validators = new ArrayList<IEventValidator>();
		listener = new ArrayList<IStateMachineListener>();
		threadPool = executor;
	}

	@Override
	public void addEventValidator( final IEventValidator validator ) {
		synchronized ( validators ) {
			validators.add( validator );
		}
	}

	@Inject
	public void addEventValidators( final Set<IEventValidator> validator ) {
		synchronized ( validators ) {
			validators.addAll( validator );
		}
	}

	@Override
	public void removeEventValidator( final IEventValidator validator ) {
		synchronized ( validators ) {
			validators.remove( validator );
		}
	}

	@Override
	public void addIStateMachineListener( final IStateMachineListener l ) {
		synchronized ( listener ) {
			listener.add( l );
		}
		l.init( this );
	}

	@Inject
	public void addIStateMachineListener( final Set<IStateMachineListener> l ) {
		synchronized ( listener ) {
			listener.addAll( l );
		}
		for ( IStateMachineListener x : l ) {
			x.init( this );
		}
	}

	@Override
	public void removeIStateMachineListener( final IStateMachineListener l ) {
		listener.remove( l );
	}

	protected ValidationResult validateEvent( final IEvent<?> event ) {
		Preconditions.checkNotNull( event );
		synchronized ( validators ) {
			for ( IEventValidator validator : validators ) {
				if ( validator.validateEvent( event ) == ValidationResult.Invalid ) {
					return ValidationResult.Invalid;
				}
			}
		}
		return ValidationResult.Valid;
	}

	protected void fireEventCommitted( final IEvent<?> event ) {
		Preconditions.checkNotNull( event );
		threadPool.execute( new Runnable() {

			@Override
			public void run() {
				synchronized ( listener ) {
					for ( IStateMachineListener l : listener ) {
						l.eventCommitted( event );
					}
				}
			}
		} );
	}

	protected void fireEventPushed( final IEvent<?> event ) {
		Preconditions.checkNotNull( event );
		threadPool.execute( new Runnable() {

			@Override
			public void run() {
				synchronized ( listener ) {
					for ( IStateMachineListener l : listener ) {
						l.eventPushed( event );
					}
				}
			}
		} );
	}

	protected void fireEventRolledBack( final IEvent<?> event ) {
		Preconditions.checkNotNull( event );
		threadPool.execute( new Runnable() {

			@Override
			public void run() {
				synchronized ( listener ) {
					for ( IStateMachineListener l : listener ) {
						l.eventRolledBack( event );
					}
				}
			}
		} );
	}
}
