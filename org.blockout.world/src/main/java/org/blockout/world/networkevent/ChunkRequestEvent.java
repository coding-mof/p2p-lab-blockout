package org.blockout.world.networkevent;

import java.net.SocketAddress;

import org.blockout.common.TileCoordinate;

/**
 * The ChunkRequestEvent can be sent and received.
 * 
 * If it is sent, the network layer will lookup the responsible node and
 * request the chunk from it.
 * 
 * If it is received, then the game state has to answer with a 
 * ChunkResponseEvent which contains the Chunk in question.
 * 
 * @author Paul Dubs
 *
 */

public class ChunkRequestEvent extends TileCoordinateBasedEvent {

	public ChunkRequestEvent(SocketAddress remoteAddress,
			SocketAddress localAddress, TileCoordinate message) {
		super(remoteAddress, localAddress, message);
	}
}
