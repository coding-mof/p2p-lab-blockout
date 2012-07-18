package org.blockout.network.reworked;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ChildChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.WriteCompletionEvent;

public interface ChannelInterceptor {

	public void init( IConnectionManager conMgr );

	public String getName();

	public void messageReceived( IConnectionManager connectionMgr, ChannelHandlerContext ctx, MessageEvent e )
			throws Exception;

	public void exceptionCaught( IConnectionManager connectionMgr, ChannelHandlerContext ctx, ExceptionEvent e )
			throws Exception;

	public void channelOpen( IConnectionManager connectionMgr, ChannelHandlerContext ctx, ChannelStateEvent e )
			throws Exception;

	public void channelBound( IConnectionManager connectionMgr, ChannelHandlerContext ctx, ChannelStateEvent e )
			throws Exception;

	public void channelConnected( IConnectionManager connectionMgr, ChannelHandlerContext ctx, ChannelStateEvent e )
			throws Exception;

	public void channelInterestChanged( IConnectionManager connectionMgr, ChannelHandlerContext ctx, ChannelStateEvent e )
			throws Exception;

	public void channelDisconnected( IConnectionManager connectionMgr, ChannelHandlerContext ctx, ChannelStateEvent e )
			throws Exception;

	public void channelUnbound( IConnectionManager connectionMgr, ChannelHandlerContext ctx, ChannelStateEvent e )
			throws Exception;

	public void channelClosed( IConnectionManager connectionMgr, ChannelHandlerContext ctx, ChannelStateEvent e )
			throws Exception;

	public void writeComplete( IConnectionManager connectionMgr, ChannelHandlerContext ctx, WriteCompletionEvent e )
			throws Exception;

	public void childChannelOpen( IConnectionManager connectionMgr, ChannelHandlerContext ctx, ChildChannelStateEvent e )
			throws Exception;

	public void childChannelClosed( IConnectionManager connectionMgr, ChannelHandlerContext ctx,
			ChildChannelStateEvent e ) throws Exception;

	public void writeRequested( IConnectionManager connectionMgr, ChannelHandlerContext ctx, MessageEvent e )
			throws Exception;

	public void bindRequested( IConnectionManager connectionMgr, ChannelHandlerContext ctx, ChannelStateEvent e )
			throws Exception;

	public void connectRequested( IConnectionManager connectionMgr, ChannelHandlerContext ctx, ChannelStateEvent e )
			throws Exception;

	public void setInterestOpsRequested( IConnectionManager connectionMgr, ChannelHandlerContext ctx,
			ChannelStateEvent e ) throws Exception;

	public void disconnectRequested( IConnectionManager connectionMgr, ChannelHandlerContext ctx, ChannelStateEvent e )
			throws Exception;

	public void unbindRequested( IConnectionManager connectionMgr, ChannelHandlerContext ctx, ChannelStateEvent e )
			throws Exception;

	public void closeRequested( IConnectionManager connectionMgr, ChannelHandlerContext ctx, ChannelStateEvent e )
			throws Exception;
}
