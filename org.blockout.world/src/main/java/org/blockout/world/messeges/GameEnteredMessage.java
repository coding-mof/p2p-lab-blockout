package org.blockout.world.messeges;

import org.blockout.network.message.IMessage;
import org.blockout.world.Chunk;

public class GameEnteredMessage implements IMessage {

	private static final long serialVersionUID = -1263905655275809964L;
	
	private Chunk chunk;
	
	public GameEnteredMessage(Chunk chunk) {
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
		GameEnteredMessage other = (GameEnteredMessage) obj;
		if (chunk == null) {
			if (other.chunk != null)
				return false;
		} else if (!chunk.equals(other.chunk))
			return false;
		return true;
	}
}
