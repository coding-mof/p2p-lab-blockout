package org.blockout.network.server;

import org.blockout.network.channel.IChannel;

public interface IConnectionListener {
	public void notify(IChannel channel);
}
