package org.blockout.network.reworked;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ChildChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.WriteCompletionEvent;

import com.google.common.base.Preconditions;

public class ChannelHandlerInterceptorAdapter extends SimpleChannelHandler {
	private final IConnectionManager	connectionMgr;
	private final ChannelInterceptor	interceptor;

	public ChannelHandlerInterceptorAdapter(final IConnectionManager connectionMgr, final ChannelInterceptor interceptor) {

		Preconditions.checkNotNull( connectionMgr );
		Preconditions.checkNotNull( interceptor );

		this.connectionMgr = connectionMgr;
		this.interceptor = interceptor;
	}

	@Override
	public void bindRequested( final ChannelHandlerContext ctx, final ChannelStateEvent e ) throws Exception {
		interceptor.bindRequested( connectionMgr, ctx, e );
	}

	@Override
	public void channelBound( final ChannelHandlerContext ctx, final ChannelStateEvent e ) throws Exception {
		interceptor.channelBound( connectionMgr, ctx, e );
	}

	@Override
	public void channelClosed( final ChannelHandlerContext ctx, final ChannelStateEvent e ) throws Exception {
		interceptor.channelClosed( connectionMgr, ctx, e );
	}

	@Override
	public void channelConnected( final ChannelHandlerContext ctx, final ChannelStateEvent e ) throws Exception {
		interceptor.channelConnected( connectionMgr, ctx, e );
	}

	@Override
	public void channelDisconnected( final ChannelHandlerContext ctx, final ChannelStateEvent e ) throws Exception {
		interceptor.channelDisconnected( connectionMgr, ctx, e );
	}

	@Override
	public void channelInterestChanged( final ChannelHandlerContext ctx, final ChannelStateEvent e ) throws Exception {
		interceptor.channelInterestChanged( connectionMgr, ctx, e );
	}

	@Override
	public void channelOpen( final ChannelHandlerContext ctx, final ChannelStateEvent e ) throws Exception {
		interceptor.channelOpen( connectionMgr, ctx, e );
	}

	@Override
	public void channelUnbound( final ChannelHandlerContext ctx, final ChannelStateEvent e ) throws Exception {
		interceptor.channelUnbound( connectionMgr, ctx, e );
	}

	@Override
	public void childChannelClosed( final ChannelHandlerContext ctx, final ChildChannelStateEvent e ) throws Exception {
		interceptor.childChannelClosed( connectionMgr, ctx, e );
	}

	@Override
	public void childChannelOpen( final ChannelHandlerContext ctx, final ChildChannelStateEvent e ) throws Exception {
		interceptor.childChannelOpen( connectionMgr, ctx, e );
	}

	@Override
	public void closeRequested( final ChannelHandlerContext ctx, final ChannelStateEvent e ) throws Exception {
		interceptor.closeRequested( connectionMgr, ctx, e );
	}

	@Override
	public void connectRequested( final ChannelHandlerContext ctx, final ChannelStateEvent e ) throws Exception {
		interceptor.connectRequested( connectionMgr, ctx, e );
	}

	@Override
	public void disconnectRequested( final ChannelHandlerContext ctx, final ChannelStateEvent e ) throws Exception {
		interceptor.disconnectRequested( connectionMgr, ctx, e );
	}

	@Override
	public void exceptionCaught( final ChannelHandlerContext ctx, final ExceptionEvent e ) throws Exception {
		interceptor.exceptionCaught( connectionMgr, ctx, e );
	}

	@Override
	public void messageReceived( final ChannelHandlerContext ctx, final MessageEvent e ) throws Exception {
		interceptor.messageReceived( connectionMgr, ctx, e );
	}

	@Override
	public void setInterestOpsRequested( final ChannelHandlerContext ctx, final ChannelStateEvent e ) throws Exception {
		interceptor.setInterestOpsRequested( connectionMgr, ctx, e );
	}

	@Override
	public void unbindRequested( final ChannelHandlerContext ctx, final ChannelStateEvent e ) throws Exception {
		interceptor.unbindRequested( connectionMgr, ctx, e );
	}

	@Override
	public void writeComplete( final ChannelHandlerContext ctx, final WriteCompletionEvent e ) throws Exception {
		interceptor.writeComplete( connectionMgr, ctx, e );
	}

	@Override
	public void writeRequested( final ChannelHandlerContext ctx, final MessageEvent e ) throws Exception {
		interceptor.writeRequested( connectionMgr, ctx, e );
	}
}
