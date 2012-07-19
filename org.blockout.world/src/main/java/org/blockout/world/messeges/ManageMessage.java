package org.blockout.world.messeges;

import java.util.ArrayList;

import org.blockout.network.dht.IHash;
import org.blockout.network.message.IMessage;
import org.blockout.world.Chunk;

public class ManageMessage implements IMessage {

	private static final long					serialVersionUID	= 5712257461491581668L;

	private final ArrayList<Chunk>				chunks;
	private final ArrayList<ArrayList<IHash>>	receivers;

	public ManageMessage() {
		chunks = new ArrayList<Chunk>();
		receivers = new ArrayList<ArrayList<IHash>>();
	}

	public void add( final Chunk c, final ArrayList<IHash> addresses ) {
		if ( c != null && addresses != null ) {
			chunks.add( c );
			receivers.add( addresses );
		}
	}

	public ArrayList<Chunk> getChunks() {
		return chunks;
	}

	public ArrayList<ArrayList<IHash>> getReceivers() {
		return receivers;
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
		ManageMessage other = (ManageMessage) obj;
		if ( chunks == null ) {
			if ( other.chunks != null ) {
				return false;
			}
		} else if ( !chunks.equals( other.chunks ) ) {
			return false;
		}
		if ( receivers == null ) {
			if ( other.receivers != null ) {
				return false;
			}
		} else if ( !receivers.equals( other.receivers ) ) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ManageMessage[chunks=" + chunks + ", receivers=" + receivers + "]";
	}
}
