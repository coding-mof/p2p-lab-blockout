package org.blockout.world.messeges;

import java.util.ArrayList;

import org.blockout.network.dht.IHash;
import org.blockout.network.message.IMessage;
import org.blockout.world.Chunk;

public class ReplicationMessage implements IMessage {

	/**
	 * number of Times each Message should be replicated
	 */
	private final int numberOfCopies = 2;
	
	/**
	 * list of peers already received this message
	 */
	private ArrayList<IHash> 	replicants;
	
	/**
	 * message that should be replicated
	 */
	private IMessage 			message;

	
	/**
	 * 
	 * @param message
	 * 			{@link IMessage} that should be replicated
	 * @param hash
	 * 			ID of the peer generating the {@link IMessage}
	 */
	public ReplicationMessage(IMessage message, IHash hash) {
		this.message = message;
		replicants = new ArrayList<IHash>();
		replicants.add(hash);
	}
	
	/**
	 * 
	 * @param hash
	 * 			the ID of the node requesting the {@link IMessage}
	 * @return
	 */
	public IMessage getMessage(IHash hash) {
		if(!replicants.contains(hash)){
			replicants.add(hash);
		}
		return message;
	}
	
	
	public ArrayList<IHash> getReplicants() {
		return replicants;
	}
	
	/**
	 * 
	 * @return true if the {@link IMessage} should be further replicated
	 */
	public boolean replicate(){
		if(replicants.size() < numberOfCopies+1){
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReplicationMessage other = (ReplicationMessage) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (replicants == null) {
			if (other.replicants != null)
				return false;
		} else if (!replicants.equals(other.replicants))
			return false;
		return true;
	}
	
	
	
	
}
