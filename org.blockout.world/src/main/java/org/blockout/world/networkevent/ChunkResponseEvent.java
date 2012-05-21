package org.blockout.world.networkevent;

import java.net.SocketAddress;

import org.blockout.network.NetworkEvent;
import org.blockout.world.Chunk;

/**
 * The ChunkResponseEvent can be sent and received.
 * 
 * If it is sent, then it was sent as a reaction to a ChunkRequestEvent.
 * 
 * If it is received, then it is received as a reaction to a ChunkRequestEvent.
 * 
 * @author Paul Dubs
 *
 */

public class ChunkResponseEvent extends NetworkEvent<Chunk> {

	public ChunkResponseEvent(SocketAddress remoteAddress,
			SocketAddress localAddress, Chunk message) {
		super(remoteAddress, localAddress, message);
	}

}