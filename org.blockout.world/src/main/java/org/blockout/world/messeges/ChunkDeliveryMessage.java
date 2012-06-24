package org.blockout.world.messeges;

import java.util.ArrayList;

import org.blockout.network.INodeAddress;
import org.blockout.network.message.IMessage;
import org.blockout.world.Chunk;

public class ChunkDeliveryMessage implements IMessage {
	
	private static final long serialVersionUID = -1551603331409674760L;
	
	private Chunk 						chunk;
	
	private ArrayList<INodeAddress> 	localPlayers;

	
	
	public ChunkDeliveryMessage(Chunk chunk, ArrayList<INodeAddress> localPlayers) {
		this.chunk = chunk;
		this.localPlayers = localPlayers;
	}


	public Chunk getChunk() {
		return chunk;
	}
	
	
	public ArrayList<INodeAddress> getLocalPlayers() {
		return localPlayers;
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
		} else if (!chunk.equals(other.chunk))
			return false;
		if (localPlayers == null) {
			if (other.localPlayers != null)
				return false;
		} else if (!localPlayers.equals(other.localPlayers))
			return false;
		return true;
	}
	
	
	
}
