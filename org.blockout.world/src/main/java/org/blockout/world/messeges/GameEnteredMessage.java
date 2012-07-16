package org.blockout.world.messeges;

import java.util.ArrayList;

import org.blockout.network.dht.IHash;
import org.blockout.network.message.IMessage;
import org.blockout.world.Chunk;

public class GameEnteredMessage implements IMessage {

	private static final long		serialVersionUID	= -1263905655275809964L;

	private final Chunk				chunk;
	private final ArrayList<IHash>	localPlayer;

	public GameEnteredMessage(final Chunk chunk, final ArrayList<IHash> localPlayer) {
		this.chunk = chunk;
		this.localPlayer = localPlayer;
	}

	public Chunk getChunk() {
		return chunk;
	}

	public ArrayList<IHash> getLocalPlayer() {
		return localPlayer;
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
		GameEnteredMessage other = (GameEnteredMessage) obj;
		if ( chunk == null ) {
			if ( other.chunk != null ) {
				return false;
			}
		} else if ( !chunk.equals( other.chunk ) ) {
			return false;
		}
		if ( localPlayer == null ) {
			if ( other.localPlayer != null ) {
				return false;
			}
		} else if ( !localPlayer.equals( other.localPlayer ) ) {
			return false;
		}
		return true;
	}

}
