package org.blockout.network.reworked;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

public class ChannelFutureListenerAdapter implements ChannelFutureListener {

	private final ConnectionFutureListener	l;

	public ChannelFutureListenerAdapter(final ConnectionFutureListener l) {
		this.l = l;
	}

	@Override
	public void operationComplete( final ChannelFuture channelFuture ) throws Exception {
		l.operationComplete( new ChannelFutureAdapter( channelFuture ) );
	}
}
