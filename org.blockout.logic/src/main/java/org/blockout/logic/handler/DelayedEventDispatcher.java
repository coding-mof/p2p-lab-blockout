package org.blockout.logic.handler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.common.IEvent;
import org.blockout.world.state.IStateMachine;
import org.blockout.world.state.IStateMachineListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class DelayedEventDispatcher implements IStateMachineListener {

	private static final Logger				logger;
	static {
		logger = LoggerFactory.getLogger( DelayedEventDispatcher.class );
	}

	private static final int				THREAD_POOL_SIZE	= 3;
	protected ScheduledThreadPoolExecutor	executor;
	protected List<IEventHandler>			eventHandler;
	protected Map<IEvent<?>, Long>			activeEvents;
	protected IStateMachine					stateMachine;

	public DelayedEventDispatcher() {
		eventHandler = new CopyOnWriteArrayList<IEventHandler>();
		activeEvents = Collections.synchronizedMap( new HashMap<IEvent<?>, Long>() );
		executor = new ScheduledThreadPoolExecutor( THREAD_POOL_SIZE );
	}

	@Inject
	public void addEventHandler( final Set<IEventHandler> handler ) {
		eventHandler.addAll( handler );
	}

	@Override
	public void init( final IStateMachine stateMachine ) {
		this.stateMachine = stateMachine;
	}

	@Override
	public void eventCommitted( final IEvent<?> event ) {
		if ( event.getDuration() == 0 ) {
			logger.info( "Direct event finished." );
			activeEvents.remove( event );
			fireEventFinished( event );
			return;
		}

		Long startTime = activeEvents.get( event );
		final long remainingMillis = (startTime + event.getDuration()) - System.currentTimeMillis();
		if ( remainingMillis <= 0 ) {
			logger.info( "Delayed event finished. Deviance: " + (-remainingMillis) + " ms" );
			activeEvents.remove( event );
			fireEventFinished( event );
			return;
		}

		logger.info( "Rescheduled event in: " + remainingMillis + " ms" );
		executor.schedule( new Runnable() {

			@Override
			public void run() {
				eventCommitted( event );
			}
		}, remainingMillis, TimeUnit.MILLISECONDS );
	}

	@Override
	public void eventPushed( final IEvent<?> event ) {
		activeEvents.put( event, System.currentTimeMillis() );
		fireEventStarted( event );
	}

	@Override
	public void eventRolledBack( final IEvent<?> event ) {
	}

	protected void fireEventStarted( final IEvent<?> event ) {
		for ( IEventHandler handler : eventHandler ) {
			handler.eventStarted( stateMachine, event );
		}
	}

	protected void fireEventFinished( final IEvent<?> event ) {
		for ( IEventHandler handler : eventHandler ) {
			handler.eventFinished( stateMachine, event );
		}
	}
}
