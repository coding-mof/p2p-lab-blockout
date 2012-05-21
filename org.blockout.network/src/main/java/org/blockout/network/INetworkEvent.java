package org.blockout.network;

import java.io.Serializable;

public interface INetworkEvent<T extends Serializable> {
	public void setRecipient(NodeInfo recipient);
	
	public NodeInfo getRecipient();
	
	public NodeInfo getSender();
	
	public T getContent();
	
	public void setContent(T event);
}
