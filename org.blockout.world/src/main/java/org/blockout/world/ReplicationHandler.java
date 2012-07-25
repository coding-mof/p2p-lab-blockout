package org.blockout.world;

import java.util.HashSet;

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

	private static final Logger				logger;
	static {
		logger = LoggerFactory.getLogger( ReplicationHandler.class );
	}

	private final IChordOverlay				chord;
	private final WorldAdapter				adapter;
	private final HashSet<TileCoordinate>	chunksToReplicate;

	public ReplicationHandler(final IChordOverlay chord, final WorldAdapter adapter, final IChunkManager manager) {
		this.chord = chord;
		this.adapter = adapter;
		manager.addListener( this );
		chunksToReplicate = new HashSet<TileCoordinate>();
		chord.addChordListener( this );
	}

	@Override
	public void chunkUpdated( final IMessage msg, final TileCoordinate c ) {
		if ( !chunksToReplicate.contains( c ) ) {
			chunksToReplicate.add( c );
			if ( msg instanceof ChunkDeliveryMessage ) {
				chord.sendMessage( new ReplicationMessage( msg, chord.getLocalId() ), chord.getSuccessor() );
				return;
			} else {
				chord.sendMessage( new ReplicationMessage( new ChunkDeliveryMessage( adapter.getChunk( c ), null ),
						chord.getLocalId() ), chord.getSuccessor() );
			}

		}

		chord.sendMessage( new ReplicationMessage( msg, chord.getLocalId() ), chord.getSuccessor() );
	}

	@Override
	public void responsibilityChanged( final IChordOverlay chord, final WrappedRange<IHash> from,
			final WrappedRange<IHash> to ) {
	}

	@Override
	public void receivedMessage( final IChordOverlay chord, final Object message, final IHash senderId ) {
		if ( message instanceof ReplicationMessage && !senderId.equals( chord.getLocalId() ) ) {
			handleReplication( (ReplicationMessage) message );
		}
	}

	private void handleReplication( final ReplicationMessage message ) {
		if ( message.getReplicants().contains( chord.getLocalId() ) ) {
			logger.debug( "dropped Replicationmessage: " + message );
			return;
		}

		logger.debug( "replicated: " + message );
		IMessage msg = message.getMessage( chord.getLocalId() );
		if ( msg instanceof ChunkDeliveryMessage ) {
			adapter.manageChunk( ((ChunkDeliveryMessage) msg).getChunk() );
		} else if ( msg instanceof EntityAddedMessage ) {
			TileCoordinate coordinate = Chunk.containingCunk( ((EntityAddedMessage) msg).getCoordinate() );
			adapter.getChunk( coordinate ).setEntityCoordinate( ((EntityAddedMessage) msg).getEntity(),
					((EntityAddedMessage) msg).getCoordinate().getX(),
					((EntityAddedMessage) msg).getCoordinate().getY() );
		} else {
			chord.sendMessage( msg, chord.getLocalId() );
		}

		if ( message.replicate() ) {
			chord.sendMessage( message, chord.getSuccessor() );
		}
	}

	@Override
	public void predecessorChanged( final IChordOverlay chord, final IHash predecessor ) {
		// clearing list of chunks
		// if you still need to replicate them someone will tell....
		chunksToReplicate.clear();
	}

	@Override
	public void successorChanged( final IChordOverlay chord, final IHash successor ) {
		// replicating all own chunks again to ensure consistency
		for ( TileCoordinate coordinate : chunksToReplicate ) {
			chord.sendMessage( new ReplicationMessage(
					new ChunkDeliveryMessage( adapter.getChunk( coordinate ), null ), chord.getLocalId() ), chord
					.getSuccessor() );
		}
	}

}
