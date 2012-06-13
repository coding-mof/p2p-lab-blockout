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
import org.blockout.network.dht.chord.messages.DHTLeavePredecessor;
import org.blockout.network.dht.chord.messages.DHTLeaveSuccessor;
import org.blockout.network.dht.chord.messages.DHTNewPredecessor;
import org.blockout.network.dht.chord.messages.DHTNewSuccessor;
import org.blockout.network.dht.chord.messages.DHTPassOnMsg;
import org.blockout.network.dht.chord.messages.DHTWelcome;
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
	public void leave() {
		logger.debug( "Leaving. State: " + state );
		if ( state == ChordState.Connected ) {
			mp.send( new DHTLeaveSuccessor( successor ), predecessor );
			mp.send( new DHTLeavePredecessor( predecessor ), successor );
			state = ChordState.Disconnected;
			logger.debug( "State: " + state );
		}
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
				DHTNewSuccessor.class, DHTLeaveSuccessor.class, DHTLeavePredecessor.class, DHTPassOnMsg.class );

		discover.addDiscoveryListener( this );

		// Send out message with my own port
		discover.sendDiscoveryMessage( new DiscoveryMsg( ownAddress.getInetAddress().getPort(), ownAddress.getNodeId() ) );

		state = ChordState.DiscoverySent;
		logger.debug( "State: " + state );

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
		if ( state == ChordState.DiscoverySent ) {
			// Assume you are alone in this world
			responsibility = new HashRange<IHash>();
			predecessor = ownAddress;
			successor = ownAddress;
			state = ChordState.Connected;
			logger.debug( "State: " + state + " (Discovery Timed out)" );
		}

	}

	@Override
	public void nodeDiscovered( final NodeInfo info ) {
		if ( state == ChordState.Connected ) {
			// This Method is called when a new Node has Broadcasted its arrival
			mp.send( new DHTFirstConnectMsg(), info );
		}
	}

	public void receive( final DHTFirstConnectMsg msg, final INodeAddress origin ) {
		logger.debug( "DHTFirstConnect: State: " + state );
		// This is the first Contact with the Chord Ring
		if ( state == ChordState.DiscoverySent ) {
			mp.send( new DHTJoin( ownAddress, ownAddress.getNodeId() ), origin );
			state = ChordState.JoinSent;
			logger.debug( "State: " + state );
		}
	}

	public void receive( final DHTJoin msg, final INodeAddress origin ) {
		logger.debug( "DHTJoin: State: " + state );
		if ( state == ChordState.Connected ) {
			if ( responsibility.contains( msg.getNodeId() ) ) {
				mp.send( new DHTWelcome( predecessor ), msg.getOrigin() );
			} else {
				// TODO: Try to Send this via DHT Routing
				mp.send( msg, successor );

				logger.debug( "Node " + msg.getNodeId() + " is not in " + responsibility );
				logger.debug( "Asking Successor " + successor );
			}
		}
	}

	public void receive( final DHTWelcome msg, final INodeAddress origin ) {
		logger.debug( "DHTWelcome: State: " + state );
		if ( state == ChordState.JoinSent ) {
			INodeAddress successor = origin;
			INodeAddress predecessor = msg.getPredecessor();

			this.successor = successor;
			this.predecessor = predecessor;
			responsibility = new HashRange<IHash>( this.predecessor.getNodeId(), ownAddress.getNodeId() );

			mp.send( new DHTNewSuccessor(), this.predecessor );
			mp.send( new DHTNewPredecessor(), this.successor );

			state = ChordState.Connected;
			logger.debug( "State: " + state );

			logger.debug( "Pre:" + this.predecessor + " Succ:" + this.successor + " Range:" + responsibility );
		}
	}

	public void receive( final DHTNewPredecessor msg, final INodeAddress origin ) {
		logger.debug( "DHTNewPredecessor: State: " + state );
		if ( state == ChordState.Connected ) {
			predecessor = origin;
			responsibility = new HashRange<IHash>( origin.getNodeId(), ownAddress.getNodeId() );

			logger.debug( "Pre:" + predecessor + " Succ:" + successor + " Range:" + responsibility );
		}
	}

	public void receive( final DHTNewSuccessor msg, final INodeAddress origin ) {
		logger.debug( "DHTNewSuccessor: State: " + state );
		if ( state == ChordState.Connected ) {
			successor = origin;
			logger.debug( "Pre:" + predecessor + " Succ:" + successor + " Range:" + responsibility );
		}

	}

	public void receive( final DHTLeavePredecessor msg, final INodeAddress origin ) {
		logger.debug( "DHTLeavePredecessor: State: " + state );
		if ( state == ChordState.Connected ) {
			predecessor = msg.getNode();
			responsibility = new HashRange<IHash>( origin.getNodeId(), ownAddress.getNodeId() );

			logger.debug( "Pre:" + predecessor + " Succ:" + successor + " Range:" + responsibility );
		}
	}

	public void receive( final DHTLeaveSuccessor msg, final INodeAddress origin ) {
		logger.debug( "DHTLeaveSuccessor: State: " + state );
		if ( state == ChordState.Connected ) {
			successor = msg.getNode();
			logger.debug( "Pre:" + predecessor + " Succ:" + successor + " Range:" + responsibility );
		}
	}

	public void receive( final DHTPassOnMsg msg, final INodeAddress origin ) {
		logger.debug( "DHTPassOnMsg: State: " + state );
		sendTo( msg.getMessage(), msg.getReceiver() );
	}

}
