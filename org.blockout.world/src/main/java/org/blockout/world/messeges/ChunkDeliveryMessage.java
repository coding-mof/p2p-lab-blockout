package org.blockout.world.messeges;

import java.util.ArrayList;

import org.blockout.network.dht.IHash;
import org.blockout.network.message.IMessage;
import org.blockout.world.Chunk;

public class ChunkDeliveryMessage implements IMessage {

	private static final long		serialVersionUID	= -1551603331409674760L;

	private final Chunk				chunk;

	private final ArrayList<IHash>	localPlayers;

	public ChunkDeliveryMessage(final Chunk chunk, final ArrayList<IHash> localPlayers) {
		this.chunk = chunk;
		this.localPlayers = localPlayers;
	}

	public Chunk getChunk() {
		return chunk;
	}

	public ArrayList<IHash> getLocalPlayers() {
		return localPlayers;
	}

	@Override
	public boolean equals( final Object obj ) {
		if ( this == obj ) {
			return true;
		}
		if ( obj == null ) {
			return false;
		}
		if ( getClass() != obj.getClass() ) {
			return false;
		}
		ChunkDeliveryMessage other = (ChunkDeliveryMessage) obj;
		if ( chunk == null ) {
			if ( other.chunk != null ) {
				return false;
			}
		} else if ( !chunk.equals( other.chunk ) ) {
			return false;
		}
		if ( localPlayers == null ) {
			if ( other.localPlayers != null ) {
				return false;
			}
		} else if ( !localPlayers.equals( other.localPlayers ) ) {
			return false;
		}
		return true;
	}

}
