package org.blockout.world.networkevent;

import java.net.SocketAddress;

import org.blockout.common.TileCoordinate;

public class TakeResponsibilityEvent extends TileCoordinateBasedEvent {

	public TakeResponsibilityEvent(SocketAddress remoteAddress,
			SocketAddress localAddress, TileCoordinate message) {
		super(remoteAddress, localAddress, message);
	}

}
