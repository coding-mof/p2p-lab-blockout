package org.blockout.network.discovery;

import java.io.Serializable;
import java.net.InetSocketAddress;

import org.blockout.network.dht.IHash;

public interface INodeAddress extends Serializable{
	public IHash getNodeId();
	public InetSocketAddress getInetAddress();
}