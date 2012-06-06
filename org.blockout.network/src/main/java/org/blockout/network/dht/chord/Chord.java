package org.blockout.network.dht.chord;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

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

@Named
public class Chord extends MessageReceiver implements IDistributedHashTable, DiscoveryListener {
	private IMessagePassing mp;
	private NodeDiscovery discover;
	private ConnectionManager connectionManager;
	private Timer timer;

	private ChordState state;

	private HashRange<IHash> responsibility;
	private INodeAddress ownAddress;
	private INodeAddress predecessor;
	private INodeAddress successor;

	@Inject
	public void setDiscover(NodeDiscovery discover, ConnectionManager mgr,
			Timer timer) {
		this.discover = discover;
		this.connectionManager = mgr;
		this.state = ChordState.Disconnected;
		this.timer = timer;
	}

	@Inject
	public void setMp(IMessagePassing mp) {
		this.mp = mp;
	}

	@Override
	public void sendTo(IMessage msg, IHash nodeId) {
		// Hello, Is it me you are looking for?
		if (this.responsibility.contains(nodeId)) {
			try {
				this.mp.notify(msg, this.ownAddress);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("I'm not responsible for " + nodeId
					+ " my range is " + this.responsibility);
			this.mp.send(new DHTPassOnMsg(msg, nodeId), this.successor);
		}
	}

	public void setUp() {
		this.ownAddress = new NodeInfo(this.connectionManager.getAddress());
		// Get Ready to receive Answers
		this.mp.addReceiver(this, DHTFirstConnectMsg.class, DHTJoin.class,
				DHTWelcome.class,
				DHTNewPredecessor.class, DHTNewSuccessor.class,
				DHTPassOnMsg.class
				);

		this.discover.addDiscoveryListener(this);

		// Send out message with my own port
		this.discover.sendDiscoveryMessage(new DiscoveryMsg(this.ownAddress
				.getInetAddress().getPort(), this.ownAddress.getNodeId()));
		// Start Listener for Discover Requests
		this.discover.startDiscoveryServer();

		final Chord that = this;
		TimerTask checkDiscoveryTimeout = new TimerTask() {
			@Override
			public void run(Timeout timeout) throws Exception {
				that.checkDiscoveryTimeOut();
			}
		};
		this.timer.newTimeout(checkDiscoveryTimeout, 5, TimeUnit.SECONDS);
	}

	protected void checkDiscoveryTimeOut() {
		if (this.state == ChordState.Disconnected) {
			// Assume you are alone in this world
			this.responsibility = new HashRange<IHash>();
			this.predecessor = this.ownAddress;
			this.successor = this.ownAddress;
			this.state = ChordState.Joined;

			System.out.println("L'etat ce moi!");
		}

	}

	@Override
	public void nodeDiscovered(NodeInfo info) {
		// This Method is called when a new Node has Broadcasted its arrival
		this.mp.send(new DHTFirstConnectMsg(), info);
	}

	public void receive(DHTFirstConnectMsg msg, INodeAddress origin){
		// This is the first Contact with the Chord Ring
		if (this.state == ChordState.Disconnected) {
			this.mp.send(
					new DHTJoin(this.ownAddress, this.ownAddress.getNodeId()),
					origin);
			this.state = ChordState.SentJoin;
		}
	}

	public void receive(DHTJoin msg, INodeAddress origin) {
		if (this.state != ChordState.Disconnected) {
			if (this.responsibility.contains(msg.getNodeId())) {
				this.mp.send(new DHTWelcome(this.predecessor), msg.getOrigin());
			} else {
				this.mp.send(msg, this.successor);
				System.out.println("Node " + msg.getNodeId() + " is not in "
						+ this.responsibility);
				System.out.println("Asking Successor " + this.successor);
			}
		}
	}

	public void receive(DHTWelcome msg, INodeAddress origin) {
		if (this.state == ChordState.SentJoin) {
			INodeAddress successor = origin;
			INodeAddress predecessor = msg.getPredecessor();

			this.successor = successor;
			this.predecessor = predecessor;
			this.responsibility = new HashRange<IHash>(
					this.predecessor.getNodeId(), this.ownAddress.getNodeId());

			this.mp.send(new DHTNewSuccessor(), this.predecessor);
			this.mp.send(new DHTNewPredecessor(), this.successor);

			this.state = ChordState.Joined;
			System.out.println("Pre:" + this.predecessor + " Succ:"
					+ this.successor + " Range:" + this.responsibility);
		}
	}

	public void receive(DHTNewPredecessor msg, INodeAddress origin) {
		this.predecessor = origin;
		this.responsibility = new HashRange<IHash>(origin.getNodeId(),
				this.ownAddress.getNodeId());
		if (this.state == ChordState.Joined) {
			this.state = ChordState.Flux;
		} else if (this.state == ChordState.Flux) {
			this.state = ChordState.Joined;
		}
		System.out.println("Pre:" + this.predecessor + " Succ:"
				+ this.successor + " Range:" + this.responsibility);
	}

	public void receive(DHTNewSuccessor msg, INodeAddress origin) {
		this.successor = origin;
		if (this.state == ChordState.Joined) {
			this.state = ChordState.Flux;
		} else if (this.state == ChordState.Flux) {
			this.state = ChordState.Joined;
		}
		System.out.println("Pre:" + this.predecessor + " Succ:"
				+ this.successor + " Range:" + this.responsibility);
	}

	public void receive(DHTPassOnMsg msg, INodeAddress origin) {
		this.sendTo(msg.getMessage(), msg.getReceiver());
	}

	public void receive(DHTLookupMsg msg, INodeAddress origin){
		System.out.println(msg.getHash());
	}

	public void receive(DHTLookupResponse msg, INodeAddress origin) {
		System.out.println(msg);
	}

}
