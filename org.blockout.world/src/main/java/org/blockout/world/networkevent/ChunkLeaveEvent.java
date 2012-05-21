package org.blockout.world.networkevent;

import java.net.SocketAddress;

import org.blockout.common.TileCoordinate;

/**
 * The ChunkLeaveEvent can be sent and received.
 * 
 * When a ChunkLeaveEvent is sent, then the Network Layer will forward this 
 * event to the responsible node, which will announce that the player has left
 * to all other players and then close the connection.
 * 
 * When a ChunkLeaveEvent is received, then the reaction depends on whether
 * the receiving node is the responsible node for this chunk.
 * 
 * If it is responsible, it has to announce the leaving to all other players, 
 * and then disconnect.
 * 
 * If it is not responsible, it just disconnects.
 * 
 * @author Paul Dubs
 *
 */

public class ChunkLeaveEvent extends TileCoordinateBasedEvent {

	public ChunkLeaveEvent(SocketAddress remoteAddress,
			SocketAddress localAddress, TileCoordinate message) {
		super(remoteAddress, localAddress, message);
	}

}
