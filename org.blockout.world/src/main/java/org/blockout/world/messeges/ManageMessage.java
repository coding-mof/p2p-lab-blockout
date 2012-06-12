package org.blockout.world.messeges;

import java.util.ArrayList;

import org.blockout.network.INodeAddress;
import org.blockout.network.message.IMessage;
import org.blockout.world.Chunk;

public class ManageMessage implements IMessage {

	private static final long serialVersionUID = 5712257461491581668L;
	
	private ArrayList<Chunk>					chunks;
	private ArrayList<ArrayList<INodeAddress>> 	receivers;
	
	public ManageMessage() {
		chunks = new ArrayList<Chunk>();
		receivers = new ArrayList<ArrayList<INodeAddress>>();
	}
	
	public void add(Chunk c, ArrayList<INodeAddress> addresses){
		if(c != null && addresses != null){
			chunks.add(c);
			receivers.add(addresses);
		}
	}

	public ArrayList<Chunk> getChunks() {
		return chunks;
	}

	public ArrayList<ArrayList<INodeAddress>> getReceivers() {
		return receivers;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ManageMessage other = (ManageMessage) obj;
		if (chunks == null) {
			if (other.chunks != null)
				return false;
		} else if (!chunks.equals(other.chunks))
			return false;
		if (receivers == null) {
			if (other.receivers != null)
				return false;
		} else if (!receivers.equals(other.receivers))
			return false;
		return true;
	}
}
