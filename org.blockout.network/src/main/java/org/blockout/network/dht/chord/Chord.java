package org.blockout.network.dht.chord;

import java.util.Hashtable;
import org.blockout.network.DiscoveryListener;
import org.blockout.network.INodeAddress;
import org.blockout.network.NodeDiscovery;
import org.blockout.network.NodeInfo;
import org.blockout.network.dht.IDistributedHashTable;
import org.blockout.network.dht.IHash;
import org.blockout.network.message.IMessagePassing;
import org.blockout.network.message.MessageReceiver;
import org.jboss.netty.channel.Channel;


public class Chord extends MessageReceiver implements IDistributedHashTable, DiscoveryListener {
	private IMessagePassing mp;
	private NodeDiscovery discover;
	private INodeAddress ownAddress;
	
	public Chord(){
		discover = new NodeDiscovery();
	}
	
	@Override
	public Channel connectTo(IHash nodeId,
			Hashtable<IHash, Channel> protoFingerTable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUp(IMessagePassing messagePassing, INodeAddress ownAddress) {
		this.mp = messagePassing;
		this.ownAddress = ownAddress;
		
		// Get Ready to receive Answers
		this.mp.addReceiver(
				this, 
				DHTFirstConnectMsg.class,
				DHTLookupMsg.class
				);
		
		discover.addDiscoveryListener(this);
		
		// Send out message with my own port
		this.discover.sendDiscoveryMessage(ownAddress.getInetAddress());	
		// Start Listener for Discover Requests
		this.discover.startDiscoveryServer();
	}

	@Override
	public void nodeDiscovered(NodeInfo info) {
		//this.mp.send(new DHTFirstConnectMsg(), info);
		this.mp.send(new DHTLookupMsg(this.ownAddress.getNodeId().getNext()), info);
	}
	
	public void receive(DHTFirstConnectMsg msg, INodeAddress origin){
		System.out.println(msg);
		System.out.println(origin.getNodeId());
		System.out.println(origin.getNodeId().getNext());
	}
	
	public void receive(DHTLookupMsg msg, INodeAddress origin){
		System.out.println(msg.getHash());
	}


}
