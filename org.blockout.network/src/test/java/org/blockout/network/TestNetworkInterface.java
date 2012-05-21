package org.blockout.network;

import java.util.HashSet;

import org.blockout.network.channel.IChannel;
import org.blockout.network.channel.IChannelFactory;
import org.blockout.network.dht.IDHTFactory;
import org.blockout.network.dht.IDistributedHashTable;
import org.blockout.network.server.IServer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class TestNetworkInterface {
	protected NetworkInterface network;
	protected IChannelFactory  channelFactory;
	protected IDHTFactory      dhtFactory;
	protected IServer          server;
	protected IDistributedHashTable dht;
	
	@Before
	public void setUp(){
		channelFactory = mock(IChannelFactory.class);
		dhtFactory = mock(IDHTFactory.class);
		server = mock(IServer.class);
		dht = mock(IDistributedHashTable.class);
		
		when(dhtFactory.createDHT(server)).thenReturn(dht);
		
		network = new NetworkInterface(
				channelFactory,
				dhtFactory,
				server
				);
	}
	
	@Test
	public void startupTest(){
		verify(dhtFactory).createDHT(server);
		verify(server).addListener(network);
	}
	
	
	@Test
	public void addListenerTest(){
		INetworkListener listener = mock(INetworkListener.class); 
		network.addListener(listener);
		
		assertTrue(network.listeners.contains(listener));
	}
	
	@Test
	public void addListenerSetTest(){
		HashSet<INetworkListener> set = new HashSet<INetworkListener>();
		INetworkListener listener1 = mock(INetworkListener.class);
		INetworkListener listener2 = mock(INetworkListener.class);
		set.add(listener1);
		set.add(listener2);
		
		network.addListener(set);
		
		assertEquals(2, network.listeners.size());
		assertTrue(network.listeners.containsAll(set));		
	}
	
	@Test
	public void notifyListenersTest(){
		HashSet<INetworkListener> set = new HashSet<INetworkListener>();
		INetworkListener listener1 = mock(INetworkListener.class);
		INetworkListener listener2 = mock(INetworkListener.class);
		set.add(listener1);
		set.add(listener2);
		
		network.addListener(set);
		INetworkEvent<?> event = mock(INetworkEvent.class);
		
		network.notify(event);
		
		verify(listener1).notify(event);
		verify(listener2).notify(event);
		
	}
	
	@Test
	public void notifyChannelTest(){
		IChannel chan1 = mock(IChannel.class);
		IChannel chan2 = mock(IChannel.class);
		
		network.notify(chan1);		
		network.notify(chan2);
		
		assertEquals(2, network.channels.size());
		assertTrue(network.channels.contains(chan1));
		assertTrue(network.channels.contains(chan2));
	}

	@Test
	public void triggerTest(){
		INetworkEvent<?> event = mock(INetworkEvent.class);
		IChannel chan1 = mock(IChannel.class);
		IChannel chan2 = mock(IChannel.class);
		
		network.notify(chan1);		
		network.notify(chan2);
		
		network.trigger(event);
		
		verify(chan1).send(event);
		verify(chan2).send(event);
	}
}
