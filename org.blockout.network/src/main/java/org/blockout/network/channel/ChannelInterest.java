package org.blockout.network.channel;

/**
 * The ChannelInterest is to indicate which kind of information a channel is 
 * interested in, and what kind of information it will send.
 * 
 * A CLIENT ChannelInterest indicates that this channel exists because the Node
 * that opened it is interested in authoritative changes on a chunk. They also 
 * will have to be notified when this node is not the coordinator for their
 * chunk anymore.
 * 
 * A CHUNKUPDATE ChannelInterest on the other hand indicates that this channel
 * will be used to send fast and uncommitted updates to every one that is near.
 * (Near as in close to the player in the game world.)
 *  
 * @author Paul Dubs
 *
 */

public enum ChannelInterest {
	CLIENT, CHUNKUPDATE
}
