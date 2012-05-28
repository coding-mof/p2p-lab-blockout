package org.blockout.network;

import java.net.InetSocketAddress;

import org.blockout.network.dht.Hash;
import org.blockout.network.dht.IHash;
import org.blockout.network.discovery.INodeAddress;

public class NodeInfo implements INodeAddress {
	private static final long serialVersionUID = -8306565426791526847L;
	private InetSocketAddress address;
	private Hash nodeId;
	
	public NodeInfo(InetSocketAddress sockAddress){
		this.address = sockAddress;
		this.nodeId = new Hash(this.address);
	}
	
	public NodeInfo(Hash nodeId){
		this.nodeId = nodeId;
	}
	
	public NodeInfo(String hostName, Integer port) {
		this.address = new InetSocketAddress(hostName, port);
		this.nodeId = new Hash(this.address);
	}

	@Override
	public IHash getNodeId() {
		return this.nodeId;
	}

	@Override
	public InetSocketAddress getInetAddress() {
		return this.address;
	}

	@Override
	public String toString() {
		return "Node[address=" + address + ", hash="+nodeId+"]";
	}
}
