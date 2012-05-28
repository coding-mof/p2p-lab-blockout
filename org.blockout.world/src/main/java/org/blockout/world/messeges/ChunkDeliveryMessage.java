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
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChunkDeliveryMessage other = (ChunkDeliveryMessage) obj;
		if (chunk == null) {
			if (other.chunk != null)
				return false;
		} else if (!chunk.getPosition().equals(other.chunk.getPosition()))
			return false;
		return true;
	}
}
