package org.blockout.network.message;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.network.ConnectionManager;
import org.blockout.network.INodeAddress;
import org.blockout.network.NodeInfo;
import org.blockout.network.dht.IDistributedHashTable;
import org.blockout.network.dht.IHash;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.MessageEvent;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

@Named
public class MessageBroker implements IMessagePassing {
	public InetSocketAddress										address;
	public INodeAddress												nodeAddress;
	protected Multimap<Class<? extends IMessage>, IMessageReceiver>	filtredReceivers;

	private IDistributedHashTable									dht;
	private ConnectionManager										connectionManager;

	public MessageBroker() {
		filtredReceivers = HashMultimap.create();
	}

	@Inject
	public void setDHT( final IDistributedHashTable dht ) {
		this.dht = dht;
	}

	@Override
	@Inject
	public void setConnectionManager( final ConnectionManager mgr ) {
		connectionManager = mgr;
	}

	public void setUp() {
		address = connectionManager.getAddress();
		nodeAddress = new NodeInfo( address );
	}

	public Set<INodeAddress> listNodes() {
		return connectionManager.getAllConnections();
	}

	@Override
	public void send( final IMessage msg, final INodeAddress recipient ) {
		final IMessageEnvelope<IMessage> envelope = new MessageEnvelope<IMessage>( msg, recipient, nodeAddress );
		final Channel chan = connectionManager.getConnection( recipient );
		chan.write( envelope );
	}

	@Override
	public void send( final IMessage msg, final IHash nodeId ) {
		Channel chan = connectionManager.getConnection( nodeId );
		if ( chan == null ) {
			dht.connectTo( nodeId );
		}
		// TODO: Get Address of Node with nodeID
		// throw new RuntimeException("Not Implemented");
		// Just send the Message back to the Sender, he is alone in this world... for now.
		try {
			this.notify(msg, nodeAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public INodeAddress getOwnAddress() {
		return nodeAddress;
	}

	// Message Handling
	@Override
	public void messageReceived( final MessageEvent e ) {
		IMessageEnvelope envelope = (IMessageEnvelope) e.getMessage();
		INodeAddress actualSender = new NodeInfo(
				((InetSocketAddress) e.getChannel().getRemoteAddress()).getHostName(), envelope.getSender()
						.getInetAddress().getPort() );
		envelope.setSender( actualSender );
		connectionManager.addConnection( envelope.getSender(), e.getChannel() );
		try {
			this.notify( envelope.getMessage(), envelope.getSender() );
		} catch ( Exception e1 ) {
			e1.printStackTrace();
			connectionManager.closeConnection( e.getChannel() );
		}
	}

	private void notify( final IMessage message, final INodeAddress sender ) throws IllegalArgumentException,
			SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Collection<IMessageReceiver> receiverList = getReceiver( message.getClass() );
		for ( IMessageReceiver receiver : receiverList ) {
			receiver.receive( message, sender );
		}
	}

	// Receiver Handling
	private Collection<IMessageReceiver> getReceiver( final Class<? extends IMessage> filterClass ) {
		Collection<IMessageReceiver> currentList;
		currentList = filtredReceivers.get( filterClass );
		return currentList;
	}

	@Override
	public void addReceiver( final IMessageReceiver receiver, final Class<? extends IMessage>... filterClasses ) {
		for ( Class<? extends IMessage> clazz : filterClasses ) {
			getReceiver( clazz ).add( receiver );
		}
	}

	@Override
	public void addReceiver( final Set<IMessageReceiver> receiver, final Class<? extends IMessage>... filterClasses ) {
		for ( Class<? extends IMessage> clazz : filterClasses ) {
			getReceiver( clazz ).addAll( receiver );
		}
	}

	@Override
	public void removeReceiver( final IMessageReceiver receiver, final Class<? extends IMessage>... filterClasses ) {
		for ( Class<? extends IMessage> clazz : filterClasses ) {
			filtredReceivers.remove( clazz, receiver );
		}
	}
}
