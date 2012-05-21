package org.blockout.network.channel;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.blockout.network.INetworkEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class TestChannel {
	protected Channel channel;
	protected org.jboss.netty.channel.Channel inner_channel;
	protected ChannelPipeline pipeline;
	
	
	@Before
	public void setUp(){
		this.inner_channel = mock(org.jboss.netty.channel.Channel.class);
		this.pipeline = mock(ChannelPipeline.class);
		
		when(this.inner_channel.getPipeline()).thenReturn(this.pipeline);
		
		
		this.channel = new Channel(ChannelInterest.CLIENT, this.inner_channel);
	}
	
	
	@Test
	public void startup(){
		verify(this.inner_channel).getPipeline();
		verify(this.pipeline).addLast(anyString(), any(SimpleChannelHandler.class));
	}
	
	@Test
	public void getChannelTest(){
		assertEquals(this.channel.getChannel(), this.inner_channel);
	}
	
	@Test
	public void addListenerTest(){
		IChannelListener listener = mock(IChannelListener.class);
		
		this.channel.addListener(listener);
		
		assertEquals(1, this.channel.listeners.size());
		assertTrue(this.channel.listeners.contains(listener));
	}
	
	@Test
	public void addListenersTest(){
		HashSet<IChannelListener> set = new HashSet<IChannelListener>();
		IChannelListener listener1 = mock(IChannelListener.class);
		IChannelListener listener2 = mock(IChannelListener.class);
		set.add(listener1);
		set.add(listener2);
		
		this.channel.addListener(set);
		
		assertEquals(2, this.channel.listeners.size());
		assertTrue(this.channel.listeners.contains(listener1));
		assertTrue(this.channel.listeners.contains(listener2));		
	}
	
	@Test
	public void sendTest(){
		INetworkEvent<?> event = mock(INetworkEvent.class);

		this.channel.send(event);
		
		verify(this.inner_channel).write(any());
	}
	
	@Test
	public void getInterestTest(){
		Set<ChannelInterest> interests = this.channel.getInterest();
		
		assertTrue(interests.contains(ChannelInterest.CLIENT));
	}
	
	@Test
	public void addInterestTest(){
		ChannelInterest clientInt = ChannelInterest.CLIENT;
		
		this.channel.addInterest(clientInt);
		
		assertTrue(this.channel.getInterest().contains(clientInt));
	}
	
	@Test
	public void addInterestsTest(){
		ChannelInterest clientInt = ChannelInterest.CLIENT;
		ChannelInterest chunkInt = ChannelInterest.CHUNKUPDATE;
		HashSet<ChannelInterest> set = new HashSet<ChannelInterest>();
		set.add(clientInt);
		set.add(chunkInt);
		
		this.channel.addInterest(set);
		
		Set<ChannelInterest> interests = this.channel.getInterest();
		
		assertTrue(interests.contains(clientInt));
		assertTrue(interests.contains(chunkInt));
	}

	@Test
	public void testNotification(){
		IChannelListener listener = mock(IChannelListener.class);
		ChannelHandlerContext ctx = mock(ChannelHandlerContext.class);
		MessageEvent e = mock(MessageEvent.class);
		ArgumentCaptor<INetworkEvent> arg = ArgumentCaptor.forClass(INetworkEvent.class);
		Serializable result = (Serializable) "Eine Nachricht";
		
		when(e.getMessage()).thenReturn(result);
		
		this.channel.addListener(listener);
		this.channel.notifyListener(ctx, e);
		
		verify(listener).notify(arg.capture());
		assertTrue(result == arg.getValue().getContent());
	}
	
	
}
