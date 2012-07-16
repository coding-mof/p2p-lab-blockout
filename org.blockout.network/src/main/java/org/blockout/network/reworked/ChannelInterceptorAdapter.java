package org.blockout.network.reworked;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ChildChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.WriteCompletionEvent;

public abstract class ChannelInterceptorAdapter implements ChannelInterceptor {

	@Override
	public void messageReceived( final IConnectionManager connectionMgr, final ChannelHandlerContext ctx,
			final MessageEvent e ) throws Exception {
	}

	@Override
	public void exceptionCaught( final IConnectionManager connectionMgr, final ChannelHandlerContext ctx,
			final ExceptionEvent e ) throws Exception {
	}

	@Override
	public void channelOpen( final IConnectionManager connectionMgr, final ChannelHandlerContext ctx,
			final ChannelStateEvent e ) throws Exception {
	}

	@Override
	public void channelBound( final IConnectionManager connectionMgr, final ChannelHandlerContext ctx,
			final ChannelStateEvent e ) throws Exception {
	}

	@Override
	public void channelConnected( final IConnectionManager connectionMgr, final ChannelHandlerContext ctx,
			final ChannelStateEvent e ) throws Exception {
	}

	@Override
	public void channelInterestChanged( final IConnectionManager connectionMgr, final ChannelHandlerContext ctx,
			final ChannelStateEvent e ) throws Exception {
	}

	@Override
	public void channelDisconnected( final IConnectionManager connectionMgr, final ChannelHandlerContext ctx,
			final ChannelStateEvent e ) throws Exception {
	}

	@Override
	public void channelUnbound( final IConnectionManager connectionMgr, final ChannelHandlerContext ctx,
			final ChannelStateEvent e ) throws Exception {
	}

	@Override
	public void channelClosed( final IConnectionManager connectionMgr, final ChannelHandlerContext ctx,
			final ChannelStateEvent e ) throws Exception {
	}

	@Override
	public void writeComplete( final IConnectionManager connectionMgr, final ChannelHandlerContext ctx,
			final WriteCompletionEvent e ) throws Exception {
	}

	@Override
	public void childChannelOpen( final IConnectionManager connectionMgr, final ChannelHandlerContext ctx,
			final ChildChannelStateEvent e ) throws Exception {
	}

	@Override
	public void childChannelClosed( final IConnectionManager connectionMgr, final ChannelHandlerContext ctx,
			final ChildChannelStateEvent e ) throws Exception {
	}

	@Override
	public void writeRequested( final IConnectionManager connectionMgr, final ChannelHandlerContext ctx,
			final MessageEvent e ) throws Exception {
	}

	@Override
	public void bindRequested( final IConnectionManager connectionMgr, final ChannelHandlerContext ctx,
			final ChannelStateEvent e ) throws Exception {
	}

	@Override
	public void connectRequested( final IConnectionManager connectionMgr, final ChannelHandlerContext ctx,
			final ChannelStateEvent e ) throws Exception {
	}

	@Override
	public void setInterestOpsRequested( final IConnectionManager connectionMgr, final ChannelHandlerContext ctx,
			final ChannelStateEvent e ) throws Exception {
	}

	@Override
	public void disconnectRequested( final IConnectionManager connectionMgr, final ChannelHandlerContext ctx,
			final ChannelStateEvent e ) throws Exception {
	}

	@Override
	public void unbindRequested( final IConnectionManager connectionMgr, final ChannelHandlerContext ctx,
			final ChannelStateEvent e ) throws Exception {
	}

	@Override
	public void closeRequested( final IConnectionManager connectionMgr, final ChannelHandlerContext ctx,
			final ChannelStateEvent e ) throws Exception {
	}
}
