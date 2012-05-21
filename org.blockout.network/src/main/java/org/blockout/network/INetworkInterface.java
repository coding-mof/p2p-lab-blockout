package org.blockout.network;

import java.util.Set;

public interface INetworkInterface {
	public void addListener(INetworkListener listener);
	public void addListener(Set<INetworkListener> listeners);
	
	public void trigger(INetworkEvent<?> event);
}
