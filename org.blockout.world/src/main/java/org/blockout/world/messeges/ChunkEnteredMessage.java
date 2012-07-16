package org.blockout.world.messeges;

import org.blockout.common.TileCoordinate;

public class ChunkEnteredMessage extends StopUpdatesMessage{

	private static final long serialVersionUID = 4403315331565296270L;

	public ChunkEnteredMessage(TileCoordinate coordinate) {
		super(coordinate);
	}

}
