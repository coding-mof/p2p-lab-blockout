package org.blockout.network.reworked;

public interface ConnectionFutureListener {
	public void operationComplete( ConnectionFuture connectFuture ) throws Exception;
}
