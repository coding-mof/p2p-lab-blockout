package org.blockout.network.reworked;

public interface FutureListener<T> {
	public void completed( ObservableFuture<T> future );
}
