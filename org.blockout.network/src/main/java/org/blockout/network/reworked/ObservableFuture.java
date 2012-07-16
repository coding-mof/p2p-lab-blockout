package org.blockout.network.reworked;

import java.util.concurrent.Future;

/**
 * Extended interface of java's {@link Future} interface that additionally
 * provides listener.
 * 
 * @author Marc-Christian Schulze
 * 
 * @param <T>
 *            The return type of the future.
 */
public interface ObservableFuture<T> extends Future<T> {
	/**
	 * Adds a listener that gets notified when the future has been completed.
	 * When the future has already been completed when the listener is added, it
	 * gets directly notified.
	 * 
	 * @param l
	 *            The listener to add.
	 */
	public void addFutureListener( FutureListener<T> l );

	/**
	 * Removes a listener.
	 * 
	 * @param l
	 *            The listener to remove.
	 */
	public void removeFutureListener( FutureListener<T> l );
}
