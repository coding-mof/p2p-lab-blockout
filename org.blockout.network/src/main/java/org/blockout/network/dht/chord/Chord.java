package org.blockout.network.dht.chord;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.blockout.network.ConnectionManager;
import org.blockout.network.INodeAddress;
import org.blockout.network.NodeInfo;
import org.blockout.network.dht.HashRange;
import org.blockout.network.dht.IDistributedHashTable;
import org.blockout.network.dht.IHash;
import org.blockout.network.dht.chord.messages.DHTFirstConnectMsg;
import org.blockout.network.dht.chord.messages.DHTJoin;
import org.blockout.network.dht.chord.messages.DHTLookupMsg;
import org.blockout.network.dht.chord.messages.DHTLookupResponse;
import org.blockout.network.discovery.DiscoveryListener;
import org.blockout.network.discovery.DiscoveryMsg;
import org.blockout.network.discovery.NodeDiscovery;
import org.blockout.network.message.IMessage;
import org.blockout.network.message.IMessagePassing;
import org.blockout.network.message.MessageReceiver;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.Timer;
import org.jboss.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Offers the Chord
 * 
 * @author Paul Dubs
 * 
 */

public class Chord extends MessageReceiver implements IDistributedHashTable, DiscoveryListener {
	private static final Logger	logger;
	static {
		logger = LoggerFactory.getLogger( Chord.class );
	}

	private IMessagePassing		mp;
	private NodeDiscovery		discover;
	private ConnectionManager	connectionManager;
	private Timer				timer;

	private ChordState			state;

	private HashRange<IHash>	responsibility;
	private INodeAddress		ownAddress;
	private INodeAddress		predecessor;
	private INodeAddress		successor;

	@Inject
	public void setDiscover( final NodeDiscovery discover, final ConnectionManager mgr, final Timer timer ) {
		this.discover = discover;
		connectionManager = mgr;
		state = ChordState.Disconnected;
		this.timer = timer;
	}

	@Inject
	public void setMp( final IMessagePassing mp ) {
		this.mp = mp;
	}

	@Override
	public void sendTo( final IMessage msg, final IHash nodeId ) {
		// Hello, Is it me you are looking for?
		if ( responsibility.contains( nodeId ) ) {
			try {
				mp.notify( msg, ownAddress );
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		} else {

			logger.debug( "I'm not responsible for " + nodeId + " my range is " + responsibility );
			mp.send( new DHTPassOnMsg( msg, nodeId ), successor );
		}
	}

	public void setUp() {
		ownAddress = new NodeInfo( connectionManager.getAddress() );
		// Get Ready to receive Answers
		mp.addReceiver( this, DHTFirstConnectMsg.class, DHTJoin.class, DHTWelcome.class, DHTNewPredecessor.class,
				DHTNewSuccessor.class, DHTPassOnMsg.class );

		discover.addDiscoveryListener( this );

		// Send out message with my own port
		discover.sendDiscoveryMessage( new DiscoveryMsg( ownAddress.getInetAddress().getPort(), ownAddress.getNodeId() ) );
		// Start Listener for Discover Requests
		discover.startDiscoveryServer();

		final Chord that = this;
		TimerTask checkDiscoveryTimeout = new TimerTask() {
			@Override
			public void run( final Timeout timeout ) throws Exception {
				that.checkDiscoveryTimeOut();
			}
		};
		timer.newTimeout( checkDiscoveryTimeout, 5, TimeUnit.SECONDS );
	}

	protected void checkDiscoveryTimeOut() {
		if ( state == ChordState.Disconnected ) {
			// Assume you are alone in this world
			responsibility = new HashRange<IHash>();
			predecessor = ownAddress;
			successor = ownAddress;
			state = ChordState.Joined;

			logger.debug( "L'etat ce moi!" );
		}

	}

	@Override
	public void nodeDiscovered( final NodeInfo info ) {
		// This Method is called when a new Node has Broadcasted its arrival
		mp.send( new DHTFirstConnectMsg(), info );
	}

	public void receive( final DHTFirstConnectMsg msg, final INodeAddress origin ) {
		// This is the first Contact with the Chord Ring
		if ( state == ChordState.Disconnected ) {
			mp.send( new DHTJoin( ownAddress, ownAddress.getNodeId() ), origin );
			state = ChordState.SentJoin;
		}
	}

	public void receive( final DHTJoin msg, final INodeAddress origin ) {
		if ( state != ChordState.Disconnected ) {
			if ( responsibility.contains( msg.getNodeId() ) ) {
				mp.send( new DHTWelcome( predecessor ), msg.getOrigin() );
			} else {
				mp.send( msg, successor );

				logger.debug( "Node " + msg.getNodeId() + " is not in " + responsibility );
				logger.debug( "Asking Successor " + successor );
			}
		}
	}

	public void receive( final DHTWelcome msg, final INodeAddress origin ) {
		if ( state == ChordState.SentJoin ) {
			INodeAddress successor = origin;
			INodeAddress predecessor = msg.getPredecessor();

			this.successor = successor;
			this.predecessor = predecessor;
			responsibility = new HashRange<IHash>( this.predecessor.getNodeId(), ownAddress.getNodeId() );

			mp.send( new DHTNewSuccessor(), this.predecessor );
			mp.send( new DHTNewPredecessor(), this.successor );

			state = ChordState.Joined;

			logger.debug( "Pre:" + this.predecessor + " Succ:" + this.successor + " Range:" + responsibility );
		}
	}

	public void receive( final DHTNewPredecessor msg, final INodeAddress origin ) {
		predecessor = origin;
		responsibility = new HashRange<IHash>( origin.getNodeId(), ownAddress.getNodeId() );
		if ( state == ChordState.Joined ) {
			state = ChordState.Flux;
		} else if ( state == ChordState.Flux ) {
			state = ChordState.Joined;
		}

		logger.debug( "Pre:" + predecessor + " Succ:" + successor + " Range:" + responsibility );
	}

	public void receive( final DHTNewSuccessor msg, final INodeAddress origin ) {
		successor = origin;
		if ( state == ChordState.Joined ) {
			state = ChordState.Flux;
		} else if ( state == ChordState.Flux ) {
			state = ChordState.Joined;
		}

		logger.debug( "Pre:" + predecessor + " Succ:" + successor + " Range:" + responsibility );
	}

	public void receive( final DHTPassOnMsg msg, final INodeAddress origin ) {
		sendTo( msg.getMessage(), msg.getReceiver() );
	}

	public void receive( final DHTLookupMsg msg, final INodeAddress origin ) {
		logger.debug( "" + msg.getHash() );
	}

	public void receive( final DHTLookupResponse msg, final INodeAddress origin ) {
		logger.debug( "" + msg );
	}
}
