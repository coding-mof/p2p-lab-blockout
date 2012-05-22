package org.blockout.world.networkevent;

import java.net.SocketAddress;

import org.blockout.common.TileCoordinate;

/**
 * The GiveUpResponsibilityEvent can be sent and received.
 * 
 * It will be sent when a node Enters 
 * @author Paul Dubs
 *
 */

public class GiveUpResponsibilityEvent extends TileCoordinateBasedEvent {

	public GiveUpResponsibilityEvent(SocketAddress remoteAddress,
			SocketAddress localAddress, TileCoordinate message) {
		super(remoteAddress, localAddress, message);
	}

}
