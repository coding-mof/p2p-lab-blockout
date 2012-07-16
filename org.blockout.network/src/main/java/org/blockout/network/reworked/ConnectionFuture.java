package org.blockout.network.reworked;

import java.util.concurrent.TimeUnit;

public interface ConnectionFuture {

	public boolean isDone();

	public boolean isCancelled();

	public boolean isSuccess();

	public Throwable getCause();

	public boolean cancel();

	public void addListener( ConnectionFutureListener connectFutureListener );

	public void removeListener( ConnectionFutureListener connectFutureListener );

	public ConnectionFuture await() throws InterruptedException;

	public ConnectionFuture awaitUninterruptibly();

	public boolean await( long paramLong, TimeUnit paramTimeUnit ) throws InterruptedException;

	public boolean await( long paramLong ) throws InterruptedException;

	public boolean awaitUninterruptibly( long paramLong, TimeUnit paramTimeUnit );

	public boolean awaitUninterruptibly( long paramLong );
}
