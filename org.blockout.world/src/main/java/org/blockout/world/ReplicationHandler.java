package org.blockout.world;

import org.blockout.common.TileCoordinate;
import org.blockout.network.dht.IHash;
import org.blockout.network.dht.WrappedRange;
import org.blockout.network.message.IMessage;
import org.blockout.network.reworked.ChordListener;
import org.blockout.network.reworked.IChordOverlay;
import org.blockout.world.messeges.ChunkDeliveryMessage;
import org.blockout.world.messeges.EntityAddedMessage;
import org.blockout.world.messeges.ReplicationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReplicationHandler implements ChunkManagerListener, ChordListener {

	private static final Logger logger;
	static {
		logger = LoggerFactory.getLogger(ReplicationHandler.class);
	}

	private final IChordOverlay 	chord;
	private final WorldAdapter 		adapter;

	public ReplicationHandler(IChordOverlay chord, WorldAdapter adapter,
			IChunkManager manager) {
		this.chord = chord;
		this.adapter = adapter;
		manager.addListener(this);
		chord.addChordListener(this);
	}

	@Override
	public void chunkUpdated(IMessage msg) {
		chord.sendMessage(new ReplicationMessage(msg, chord.getLocalId()),
				chord.getSuccessor());
	}

	@Override
	public void responsibilityChanged(IChordOverlay chord,
			WrappedRange<IHash> from, WrappedRange<IHash> to) {
	}

	@Override
	public void receivedMessage(IChordOverlay chord, Object message,
			IHash senderId) {
		if (message instanceof ReplicationMessage
				&& !senderId.equals(chord.getLocalId())) {
			handleReplication((ReplicationMessage) message);
		}
	}

	private void handleReplication(ReplicationMessage message) {
		if (message.getReplicants().contains(chord.getLocalId())) {
			logger.debug("dropped Replicationmessage: " + message);
			return;
		}

		logger.debug("replicated: " + message);
		IMessage msg = message.getMessage(chord.getLocalId());
		if (msg instanceof ChunkDeliveryMessage) {
			adapter.manageChunk(((ChunkDeliveryMessage) msg).getChunk());
		} else if (msg instanceof EntityAddedMessage) {
			TileCoordinate coordinate = Chunk
					.containingCunk(((EntityAddedMessage) msg).getCoordinate());
			adapter.getChunk(coordinate).setEntityCoordinate(
					((EntityAddedMessage) msg).getEntity(),
					((EntityAddedMessage) msg).getCoordinate().getX(),
					((EntityAddedMessage) msg).getCoordinate().getY());
		} else {
			chord.sendMessage(msg, chord.getLocalId());
		}

		if (message.replicate()) {
			chord.sendMessage(message, chord.getSuccessor());
		}
	}

    @Override
    public void predecessorChanged( IChordOverlay chord, IHash predecessor ) {
        // TODO Auto-generated method stub

    }

    @Override
    public void successorChanged( IChordOverlay chord, IHash successor ) {
        // TODO Auto-generated method stub

    }

}
