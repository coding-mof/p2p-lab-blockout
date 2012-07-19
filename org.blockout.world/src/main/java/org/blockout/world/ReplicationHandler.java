package org.blockout.world;

import org.blockout.common.TileCoordinate;
import org.blockout.network.dht.IHash;
import org.blockout.network.dht.WrappedRange;
import org.blockout.network.message.IMessage;
import org.blockout.network.reworked.ChordListener;
import org.blockout.network.reworked.IChordOverlay;
import org.blockout.world.messeges.ChunkDeliveryMessage;
import org.blockout.world.messeges.ReplicationMessage;

public class ReplicationHandler implements ChunkManagerListener, ChordListener{
	
	private final IChordOverlay		chord;
	private final WorldAdapter		adapter;

	public ReplicationHandler(IChordOverlay chord, WorldAdapter adapter, IChunkManager manager) {
		this.chord = chord;
		this.adapter = adapter;
		manager.addListener(this);
		chord.addChordListener(this);
	}

	@Override
	public void chunkUpdated(IMessage msg) {
		chord.sendMessage(new ReplicationMessage(msg, chord.getLocalId()), chord.getSuccessor());		
	}

	@Override
	public void responsibilityChanged(IChordOverlay chord,
			WrappedRange<IHash> from, WrappedRange<IHash> to) {	
	}

	LoggerFrame logger = new LoggerFrame("Replication");
	@Override
	public void receivedMessage(IChordOverlay chord, Object message,
			IHash senderId) {
		if(message instanceof ReplicationMessage && !((ReplicationMessage) message).getReplicants().contains(chord.getLocalId())){
			handleReplication((ReplicationMessage)message);			
		}
	}

	private void handleReplication(ReplicationMessage message) {
		IMessage msg = message.getMessage(chord.getLocalId());
		if(msg instanceof ChunkDeliveryMessage){
			adapter.manageChunk(((ChunkDeliveryMessage) msg).getChunk());
		}else{
			chord.sendMessage(msg, chord.getLocalId());
		}
		
		if (message.replicate()) {
			chord.sendMessage(message, chord.getSuccessor());
		}
	}

}
