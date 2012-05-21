package org.blockout.network;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.blockout.network.channel.IChannel;
import org.blockout.network.channel.IChannelFactory;
import org.blockout.network.channel.IChannelListener;
import org.blockout.network.dht.IDHTFactory;
import org.blockout.network.dht.IDistributedHashTable;
import org.blockout.network.server.IConnectionListener;
import org.blockout.network.server.IServer;

public class NetworkInterface implements INetworkInterface, IConnectionListener, IChannelListener {

	protected CopyOnWriteArraySet<INetworkListener> listeners;
	protected List<IChannel> channels;
	
	private IChannelFactory channelFactory;
	private IDHTFactory dhtFactory;
	
	private IServer server;
	private IDistributedHashTable dht;
 
	
	
	public NetworkInterface(IChannelFactory channelFactory,
			IDHTFactory dhtFactory, IServer server) {
		this.listeners = new CopyOnWriteArraySet<INetworkListener>();
		this.channels = new LinkedList<IChannel>();
		
		this.channelFactory = channelFactory;
		this.dhtFactory = dhtFactory;
		
		this.server = server;
		this.dht = this.dhtFactory.createDHT(server);
		
		this.server.addListener(this);
	}

	@Override
	public void addListener(INetworkListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void addListener(Set<INetworkListener> listeners) {
		this.listeners.addAll(listeners);
	}

	@Override
	public void trigger(INetworkEvent<?> event) {
		// TODO: Need to differentiate Events
		for(IChannel channel: this.channels){
			channel.send(event);
		}
	}

	@Override
	public void notify(IChannel channel) {
		this.channels.add(channel);
		channel.addListener(this);		
	}

	@Override
	public void notify(INetworkEvent<?> event) {
		// TODO: Differentiate Events
		for(INetworkListener listener: this.listeners){
			listener.notify(event);
		}
		
	}

}
