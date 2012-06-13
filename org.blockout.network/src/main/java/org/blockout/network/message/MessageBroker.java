package org.blockout.network.message;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.blockout.network.ConnectionManager;
import org.blockout.network.INodeAddress;
import org.blockout.network.NodeInfo;
import org.blockout.network.dht.Hash;
import org.blockout.network.dht.IDistributedHashTable;
import org.blockout.network.dht.IHash;
import org.blockout.network.dht.chord.DHTPassOnMsg;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Provides message passing facilities to the rest of the System.
 * 
 * It needs a DHT and a ConnectionManager to work correctly. They are injected
 * via Spring at the moment.
 * 
 * @author Paul Dubs
 * 
 */

public class MessageBroker implements IMessagePassing, Runnable {
	private static final Logger										logger;
	static {
		logger = LoggerFactory.getLogger( MessageBroker.class );
	}

	// Map of registered receivers
	protected Multimap<Class<? extends IMessage>, IMessageReceiver>	filtredReceivers;

	// DHT for addressing via nodeID
	private IDistributedHashTable									dht;

	// ConnectionMannger to abstract the messy connection management away
	private ConnectionManager										connectionManager;

	// Message Queues for Incoming, Outgoing and Internal Notification Messages
	private enum MessageType {
		OUTGOING, INCOMING
	};

	private final LinkedBlockingQueue<MessageType>			notifications;
	private final LinkedList<IMessageEnvelope<IMessage>>	outgoing;
	private final LinkedList<MessageEvent>					incoming;
	private final TaskExecutor								exec;

	// First part of Initialization Sequence, setDHT and setConnectionManager
	// also have to be called and then the setUp method.
	public MessageBroker(final TaskExecutor exec) {
		filtredReceivers = HashMultimap.create();
		notifications = new LinkedBlockingQueue<MessageType>();
		outgoing = new LinkedList<IMessageEnvelope<IMessage>>();
		incoming = new LinkedList<MessageEvent>();
		this.exec = exec;
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

	// Mandatory
	public void setUp() {
		exec.execute( this );
	}

	public Set<INodeAddress> listNodes() {
		return connectionManager.getAllConnections();
	}

	@Override
	/**
	 * Puts the Message in an Envelope that contains this node's address as the
	 * sender and puts it into the outgoing message queue.
	 */
	public void send( final IMessage msg, final INodeAddress recipient ) {
		IMessageEnvelope<IMessage> envelope = new MessageEnvelope<IMessage>( msg, recipient, getOwnAddress() );
		outgoing.offer( envelope );
		notifications.offer( MessageType.OUTGOING );
	}

	@Override
	/**
	 * Packs the Message into a DHT Message, so that it can be redirected, and
	 * tries to send that message.
	 */
	public void send( final IMessage msg, final IHash nodeId ) {
		this.send( new DHTPassOnMsg( msg, nodeId ), new NodeInfo( (Hash) nodeId ) );
	}

	@Override
	public INodeAddress getOwnAddress() {
		return new NodeInfo( connectionManager.getAddress() );
	}

	// Message Handling
	@Override
	/**
	 * Receives the Message, and puts it in the according queue, to be processed
	 * later.
	 */
	public void messageReceived( final MessageEvent e ) {
		incoming.offer( e );
		notifications.offer( MessageType.INCOMING );
	}

	@Override
	/**
	 * Notifies all interested receivers about the given message
	 */
	public void notify( final IMessage message, final INodeAddress sender ) throws IllegalArgumentException,
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

	@Override
	/**
	 * The Runloop, the messages are dealt with in their order of insertion.
	 */
	public void run() {
		MessageType type;
		while ( true ) {
			try {
				type = notifications.poll( 5, TimeUnit.SECONDS );

				if ( type == MessageType.INCOMING ) {
					receive_message();
				} else if ( type == MessageType.OUTGOING ) {
					send_message();
				}

			} catch ( InterruptedException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Gets a message from the incoming queue, corrects its sender and notifies
	 * everybody who cares about it.
	 */
	private void receive_message() {
		MessageEvent event = incoming.poll();

		IMessageEnvelope<IMessage> envelope = (IMessageEnvelope<IMessage>) event.getMessage();

		logger.debug( "Received Message in Envelope: " + envelope );

		INodeAddress actualSender = new NodeInfo(
				((InetSocketAddress) event.getChannel().getRemoteAddress()).getHostName(), envelope.getSender()
						.getInetAddress().getPort(), envelope.getSender().getNodeId() );

		envelope.setSender( actualSender );
		connectionManager.addConnection( envelope.getSender(), event.getChannel() );
		try {
			this.notify( envelope.getMessage(), envelope.getSender() );
		} catch ( Exception e1 ) {
			e1.printStackTrace();
			logger.warn( "Closing channel " + event.getChannel() + " due to exception.", e1 );
			connectionManager.closeConnection( event.getChannel() );
		}
	}

	/**
	 * Gets a message from the outgoing queue, tries to send it directly, and if
	 * no direct connection is found it is routed over the DHT.
	 */
	private void send_message() {
		IMessageEnvelope<IMessage> envelope = outgoing.poll();
		INodeAddress recipient = envelope.getRecipient();
		Channel chan;

		logger.debug( "sending: " + envelope.getMessage() + " to Address " + envelope.getRecipient() );

		if ( recipient.getInetAddress() == null ) {
			chan = connectionManager.getConnection( recipient.getNodeId() );
		} else {
			chan = connectionManager.getConnection( recipient );
		}

		if ( chan == null ) {
			dht.sendTo( envelope.getMessage(), recipient.getNodeId() );
		} else {
			chan.write( envelope );
		}
	}

}
