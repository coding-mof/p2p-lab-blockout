package org.blockout.world.networkevent;

import java.net.SocketAddress;

import org.blockout.common.TileCoordinate;
import org.blockout.network.NetworkEvent;

public class TileCoordinateBasedEvent extends NetworkEvent<TileCoordinate> {

	public TileCoordinateBasedEvent(SocketAddress remoteAddress,
			SocketAddress localAddress, TileCoordinate message) {
		super(remoteAddress, localAddress, message);
	}

}