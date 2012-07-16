package org.blockout.network.reworked;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.blockout.network.dht.IHash;

import com.google.common.base.Preconditions;

/**
 * Adapter class that represents a pending successor lookup.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class FindSuccessorFuture implements ObservableFuture<IHash> {

	private final Object							lock	= new Object();
	private final Set<? super FindSuccessorFuture>	managedSet;
	private IHash									successor;
	private boolean									canceled;
	private final IHash								key;
	private final List<FutureListener<IHash>>		listener;

	/**
	 * Creates a new instances and adds it to the given {@link Set}. The future
	 * automatically removes itself from the managed set when is has been
	 * canceled or completed.
	 * 
	 * @param managedSet
	 * @param key
	 */
	public FindSuccessorFuture(final Set<? super FindSuccessorFuture> managedSet, final IHash key) {

		Preconditions.checkNotNull( managedSet );
		Preconditions.checkNotNull( key );

		this.managedSet = managedSet;
		managedSet.add( this );
		this.key = key;
		listener = new CopyOnWriteArrayList<FutureListener<IHash>>();
	}

	/**
	 * The key we are looking for it's successor.
	 * 
	 * @return
	 */
	public IHash getKey() {
		return key;
	}

	/**
	 * Completes this future by setting the found successor's id and notifying
	 * all listener. This method remove the future instance from the managed set
	 * passed in the constructor.
	 * 
	 * @param successor
	 */
	public void complete( final IHash successor ) {
		synchronized ( lock ) {
			if ( isDone() ) {
				// We are too late
				return;
			}
			this.successor = successor;
			managedSet.remove( this );
			lock.notifyAll();
		}
		fireFutureCompleted();
	}

	/**
	 * Aborts the lookup for the successor. This method remove the future
	 * instance from the managed set passed in the constructor.
	 */
	@Override
	public boolean cancel( final boolean mayInterruptIfRunning ) {
		synchronized ( lock ) {
			if ( successor != null ) {
				return false;
			}
			managedSet.remove( this );
			canceled = true;
			lock.notifyAll();
		}
		fireFutureCompleted();
		return true;
	}

	@Override
	public boolean isCancelled() {
		synchronized ( lock ) {
			return canceled;
		}
	}

	@Override
	public boolean isDone() {
		synchronized ( lock ) {
			return successor != null || canceled;
		}
	}

	@Override
	public IHash get() throws InterruptedException, ExecutionException {
		synchronized ( lock ) {
			if ( successor == null ) {
				lock.wait();
			}
			return successor;
		}
	}

	@Override
	public IHash get( final long timeout, final TimeUnit unit ) throws InterruptedException, ExecutionException,
			TimeoutException {
		synchronized ( lock ) {
			if ( successor == null ) {
				lock.wait( unit.toMillis( timeout ) );
			}
			return successor;
		}
	}

	private void fireFutureCompleted() {
		synchronized ( listener ) {
			for ( FutureListener<IHash> l : listener ) {
				l.completed( this );
			}
		}
	}

	@Override
	public void addFutureListener( final FutureListener<IHash> l ) {
		synchronized ( listener ) {
			listener.add( l );
			synchronized ( lock ) {
				if ( isDone() ) {
					l.completed( this );
				}
			}
		}
	}

	@Override
	public void removeFutureListener( final FutureListener<IHash> l ) {
		listener.remove( l );
	}
}
