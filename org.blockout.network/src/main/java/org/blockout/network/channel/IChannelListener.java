package org.blockout.network.channel;

import org.blockout.network.INetworkEvent;

public interface IChannelListener {
	public void notify(INetworkEvent<?> event);
}
