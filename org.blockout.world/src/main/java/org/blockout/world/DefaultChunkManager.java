package org.blockout.world;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.common.TileCoordinate;
import org.blockout.network.INodeAddress;
import org.blockout.network.dht.Hash;
import org.blockout.network.message.IMessagePassing;
import org.blockout.network.message.MessageReceiver;
import org.blockout.world.event.IEvent;
import org.blockout.world.messeges.ChuckRequestMessage;
import org.blockout.world.messeges.ChunkDeliveryMessage;
import org.blockout.world.messeges.StateMessage;
import org.blockout.world.messeges.StopUpdatesMessage;
import org.blockout.world.state.IStateMachine;
import org.blockout.world.state.IStateMachineListener;

/**
 * 
 * @author Konstantin Ramig
 */
@Named
public class DefaultChunkManager extends MessageReceiver implements IChunkManager, IStateMachineListener {

	private IStateMachine												stateMachine;

	private final WorldAdapter											worldAdapter;

	@Inject
	private final IMessagePassing										messagePassing;

	private final Hashtable<TileCoordinate, ArrayList<INodeAddress>>	receiver;

	@Inject
	public DefaultChunkManager(final WorldAdapter worldAdapter, final IMessagePassing network) {
		super();
		receiver = new Hashtable<TileCoordinate, ArrayList<INodeAddress>>();
		this.worldAdapter = worldAdapter;
		this.worldAdapter.setManager( this );
		messagePassing = network;
		network.addReceiver( this, ChuckRequestMessage.class );
		network.addReceiver( this, ChunkDeliveryMessage.class );
		network.addReceiver( this, StateMessage.class );
		network.addReceiver( this, StopUpdatesMessage.class );
	}

	@Override
	public void init( final IStateMachine stateMachine ) {
		this.stateMachine = stateMachine;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void eventCommitted( final IEvent<?> event ) {
		TileCoordinate coordinate = Chunk.containingCunk( event.getResponsibleTile() );
		if ( receiver.containsKey( coordinate ) ) {
			for ( INodeAddress address : receiver.get( coordinate ) ) {
				messagePassing.send( new StateMessage( event, StateMessage.COMMIT_MESSAGE ), address );
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void eventPushed( final IEvent<?> event ) {
		TileCoordinate coordinate = Chunk.containingCunk( event.getResponsibleTile() );
		if ( receiver.containsKey( coordinate ) ) {
			messagePassing.send( new StateMessage( event, StateMessage.Push_MESSAGE ), new Hash( coordinate ) );
		} else {
			// BUG? doesn't this raise another commitEvent ?
			stateMachine.commitEvent( event );
		}
		// TODO add local connections
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void eventRolledBack( final IEvent<?> event ) {
		TileCoordinate coordinate = Chunk.containingCunk( event.getResponsibleTile() );
		if ( receiver.containsKey( coordinate ) ) {
			for ( INodeAddress address : receiver.get( coordinate ) ) {
				messagePassing.send( new StateMessage( event, StateMessage.ROLLBAK_MESSAGE ), address );
			}
		}

		// TODO add local connections
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestChunk( final TileCoordinate position ) {
		messagePassing.send( new ChuckRequestMessage( position ), new Hash( position ) );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stopUpdating( final TileCoordinate position ) {
		messagePassing.send( new StopUpdatesMessage( position ), new Hash( position ) );
	}

	public void receive( final StateMessage msg, final INodeAddress origin ) {
		switch ( msg.getType() ) {
			case StateMessage.ROLLBAK_MESSAGE:
				stateMachine.rollbackEvent( msg.getEvent() );
				break;
			case StateMessage.COMMIT_MESSAGE:
				stateMachine.commitEvent( msg.getEvent() );
				break;
			case StateMessage.Push_MESSAGE:
				stateMachine.pushEvent( msg.getEvent() );
				break;
			default:
				break;
		}
	}

	public void receive( final ChuckRequestMessage msg, final INodeAddress origin ) {
		Chunk c = worldAdapter.getChunk( msg.getCoordinate() );
		if ( !receiver.containsKey( c.getPosition() ) ) {
			receiver.put( c.getPosition(), new ArrayList<INodeAddress>() );
		}
		receiver.get( c.getPosition() ).add( origin );
		messagePassing.send( new ChunkDeliveryMessage( c ), origin );
	}

	public void receive( final ChunkDeliveryMessage msg, final INodeAddress origin ) {
		Chunk c = msg.getChunk();
		if ( receiver.containsKey( c.getPosition() ) ) {
			receiver.get( c.getPosition() ).remove( origin );
		}
		worldAdapter.responseChunk( c );
	}

	public void receive( final StopUpdatesMessage msg, final INodeAddress origin ) {
		if ( receiver.containsKey( msg.getCoordinate() ) ) {
			receiver.get( msg.getCoordinate() ).remove( origin );
		}
		// TODO remove from local connections
	}

}
