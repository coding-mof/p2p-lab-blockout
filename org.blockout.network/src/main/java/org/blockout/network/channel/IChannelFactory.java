package org.blockout.network.channel;

import java.util.Set;

import org.blockout.network.NodeInfo;

public interface IChannelFactory {
	public IChannel createChannel(NodeInfo node);
	public IChannel createChannel(NodeInfo node, ChannelInterest intent);
	public IChannel createChannel(NodeInfo node, Set<ChannelInterest> intent);
}
