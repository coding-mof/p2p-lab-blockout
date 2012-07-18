package org.blockout.network.reworked;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;

import com.google.common.collect.Maps;

class ConnectionFutureAdapter implements ConnectionFuture {

	private final Map<ConnectionFutureListener, ChannelFutureListenerAdapter>	mapping;
	private final ChannelFuture													future;

	public ConnectionFutureAdapter(final ChannelFuture future) {
		this.future = future;
		mapping = Maps.newHashMap();
	}

	@Override
	public boolean isDone() {
		return future.isDone();
	}

	@Override
	public boolean isCancelled() {
		return future.isCancelled();
	}

	@Override
	public boolean isSuccess() {
		return future.isSuccess();
	}

	@Override
	public Throwable getCause() {
		return future.getCause();
	}

	@Override
	public boolean cancel() {
		return future.cancel();
	}

	@Override
	public void addListener( final ConnectionFutureListener connectFutureListener ) {
		ChannelFutureListenerAdapter adapter = new ChannelFutureListenerAdapter( connectFutureListener );
		mapping.put( connectFutureListener, adapter );
		future.addListener( adapter );
	}

	@Override
	public void removeListener( final ConnectionFutureListener connectFutureListener ) {
		ChannelFutureListenerAdapter adapter = mapping.remove( connectFutureListener );
		future.removeListener( adapter );
	}

	@Override
	public ConnectionFuture await() throws InterruptedException {
		return new ConnectionFutureAdapter( future.await() );
	}

	@Override
	public ConnectionFuture awaitUninterruptibly() {
		return new ConnectionFutureAdapter( future.awaitUninterruptibly() );
	}

	@Override
	public boolean await( final long paramLong, final TimeUnit paramTimeUnit ) throws InterruptedException {
		return future.await( paramLong, paramTimeUnit );
	}

	@Override
	public boolean await( final long paramLong ) throws InterruptedException {
		return future.await( paramLong );
	}

	@Override
	public boolean awaitUninterruptibly( final long paramLong, final TimeUnit paramTimeUnit ) {
		return future.awaitUninterruptibly( paramLong, paramTimeUnit );
	}

	@Override
	public boolean awaitUninterruptibly( final long paramLong ) {
		return future.awaitUninterruptibly( paramLong );
	}

	@Override
	public SocketAddress getRemoteAddress() {
		return future.getChannel().getRemoteAddress();
	}

	@Override
	public Channel getChannel() {
		return future.getChannel();
	}
}
