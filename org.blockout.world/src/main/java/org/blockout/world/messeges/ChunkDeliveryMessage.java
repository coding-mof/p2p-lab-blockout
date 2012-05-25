package org.blockout.world.messeges;

import org.blockout.network.message.IMessage;
import org.blockout.world.Chunk;

public class ChunkDeliveryMessage implements IMessage {
	
	private static final long serialVersionUID = -1551603331409674760L;
	
	private Chunk chunk;

	
	
	public ChunkDeliveryMessage(Chunk chunk) {
		super();
		this.chunk = chunk;
	}


	public Chunk getChunk() {
		return chunk;
	}
	
	
}
