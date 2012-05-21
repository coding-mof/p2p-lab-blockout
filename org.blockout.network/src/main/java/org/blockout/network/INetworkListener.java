package org.blockout.network;

public interface INetworkListener {
	public void notify(INetworkEvent<?> event);
}
