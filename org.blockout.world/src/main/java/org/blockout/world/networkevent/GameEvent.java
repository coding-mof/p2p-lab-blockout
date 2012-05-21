package org.blockout.world.networkevent;

import java.net.SocketAddress;

import org.blockout.network.NetworkEvent;
import org.blockout.world.event.IEvent;

/**
 * This Event encapsulates a Game State Event. It can be sent and received.
 * @author Paul Dubs
 *
 * @param <T>
 */

public class GameEvent<T extends IEvent<T>> extends NetworkEvent<IEvent<T>> {

	public GameEvent(SocketAddress remoteAddress, SocketAddress localAddress,
			IEvent<T> message) {
		super(remoteAddress, localAddress, message);
	}

}
