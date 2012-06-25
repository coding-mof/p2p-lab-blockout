package org.blockout.world;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.inject.Inject;

import org.blockout.common.TileCoordinate;
import org.blockout.network.INodeAddress;
import org.blockout.network.dht.Hash;
import org.blockout.network.message.IMessagePassing;
import org.blockout.network.message.MessageReceiver;
import org.blockout.world.entity.Player;
import org.blockout.world.event.IEvent;
import org.blockout.world.messeges.ChuckRequestMessage;
import org.blockout.world.messeges.ChunkDeliveryMessage;
import org.blockout.world.messeges.EnterGameMessage;
import org.blockout.world.messeges.EntityAddedMessage;
import org.blockout.world.messeges.GameEnteredMessage;
import org.blockout.world.messeges.IComparator;
import org.blockout.world.messeges.ManageMessage;
import org.blockout.world.messeges.StateMessage;
import org.blockout.world.messeges.StopUpdatesMessage;
import org.blockout.world.messeges.UnmanageMessage;
import org.blockout.world.state.IStateMachine;
import org.blockout.world.state.IStateMachineListener;
import org.blockout.world.state.ValidationResult;

/**
 * 
 * @author Konstantin Ramig
 */
public class DefaultChunkManager extends MessageReceiver implements IChunkManager, IStateMachineListener {

	private IStateMachine												stateMachine;

	private final WorldAdapter											worldAdapter;

	private final IMessagePassing										messagePassing;

	private final Hashtable<TileCoordinate, ArrayList<INodeAddress>>	receiver;

	private final Hashtable<TileCoordinate, ArrayList<INodeAddress>>	local;

	@Inject
	public DefaultChunkManager(final WorldAdapter worldAdapter, final IMessagePassing network) {
		super();
		receiver = new Hashtable<TileCoordinate, ArrayList<INodeAddress>>();
		local = new Hashtable<TileCoordinate, ArrayList<INodeAddress>>();
		this.worldAdapter = worldAdapter;
		this.worldAdapter.setManager( this );
		messagePassing = network;
		network.addReceiver( this, ChuckRequestMessage.class, ChunkDeliveryMessage.class, StateMessage.class,
				StopUpdatesMessage.class, EnterGameMessage.class, ManageMessage.class, UnmanageMessage.class,
				GameEnteredMessage.class, EntityAddedMessage.class );
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
		if ( !receiver.containsKey( coordinate ) ) {
			messagePassing.send( new StateMessage( event, StateMessage.PUSH_MESSAGE ), new Hash( coordinate ) );
			if ( local.containsKey( coordinate ) ) {
				for ( INodeAddress address : local.get( coordinate ) ) {
					messagePassing.send( new StateMessage( event, StateMessage.PUSH_MESSAGE ), address );
				}
			}

		} else {
			stateMachine.commitEvent( event );
		}
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
		if ( local.contains( coordinate ) ) {
			for ( INodeAddress address : local.get( coordinate ) ) {
				messagePassing.send( new StateMessage( event, StateMessage.ROLLBAK_MESSAGE ), address );
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestChunk( final TileCoordinate position ) {
		local.put( position, new ArrayList<INodeAddress>() );
		messagePassing.send( new ChuckRequestMessage( position ), new Hash( position ) );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stopUpdating( final TileCoordinate position ) {
		messagePassing.send( new StopUpdatesMessage( position ), new Hash( position ) );
		if ( local.containsKey( position ) ) {
			for ( INodeAddress address : local.get( position ) ) {
				messagePassing.send( new StopUpdatesMessage( position ), address );
			}
		}
		local.remove( position );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void enterGame( final Player player ) {
		messagePassing.send( new EnterGameMessage( player ), new Hash( new TileCoordinate( 0, 0 ) ) );
	}

	public void receive( final StateMessage msg, final INodeAddress origin ) {
		switch ( msg.getType() ) {
			case StateMessage.ROLLBAK_MESSAGE:
				stateMachine.rollbackEvent( msg.getEvent() );
				break;
			case StateMessage.COMMIT_MESSAGE:
				stateMachine.commitEvent( msg.getEvent() );
				break;
			case StateMessage.PUSH_MESSAGE:
				ValidationResult result = stateMachine.pushEvent( msg.getEvent() );
				if ( result == ValidationResult.Invalid ) {
					eventRolledBack( msg.getEvent() );
				}
				if ( receiver.containsKey( Chunk.containingCunk( msg.getEvent().getResponsibleTile() ) ) ) {
					if ( result == ValidationResult.Valid ) {
						stateMachine.commitEvent( msg.getEvent() );
					}
				}
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
		messagePassing.send( new ChunkDeliveryMessage( c, (ArrayList<INodeAddress>) receiver.get( msg.getCoordinate() )
				.clone() ), origin );
		receiver.get( c.getPosition() ).add( origin );

	}

	public void receive( final ChunkDeliveryMessage msg, final INodeAddress origin ) {
		Chunk c = msg.getChunk();
		if ( receiver.containsKey( c.getPosition() ) ) {
			receiver.get( c.getPosition() ).remove( origin );
		}
		if ( local.containsKey( c.getPosition() ) ) {
			receiver.put( c.getPosition(), msg.getLocalPlayers() );
		}
		worldAdapter.responseChunk( c );
	}

	public void receive( final StopUpdatesMessage msg, final INodeAddress origin ) {
		if ( receiver.containsKey( msg.getCoordinate() ) ) {
			receiver.get( msg.getCoordinate() ).remove( origin );
		}
		if ( local.contains( msg.getCoordinate() ) ) {
			local.get( msg.getCoordinate() ).remove( origin );
		}
	}

	public void receive( final UnmanageMessage msg, final INodeAddress origin ) {
		IComparator comparator = msg.getComparator();
		ManageMessage manageMessage = new ManageMessage();
		for ( TileCoordinate coordinate : receiver.keySet() ) {
			if ( !comparator.compare( coordinate ) ) {
				manageMessage.add( worldAdapter.unmanageChunk( coordinate ), receiver.remove( coordinate ) );
			}
		}
		messagePassing.send( manageMessage, origin );
	}

	public void receive( final ManageMessage msg, final INodeAddress origin ) {

		ArrayList<Chunk> chunks = msg.getChunks();
		ArrayList<ArrayList<INodeAddress>> addresses = msg.getReceivers();

		for ( int i = 0; i < chunks.size(); i++ ) {
			receiver.put( chunks.get( i ).getPosition(), addresses.get( i ) );
			worldAdapter.manageChunk( chunks.get( i ) );
		}

	}

	public void receive( final EnterGameMessage msg, final INodeAddress origin ) {
		Chunk c = worldAdapter.getChunk( new TileCoordinate( 0, 0 ) );

		// TODO better player placement
		boolean set = false;
		for ( int i = 1; i < Chunk.CHUNK_SIZE - 1; i++ ) {
			for ( int j = 1; j < Chunk.CHUNK_SIZE - 1; j++ ) {
				if ( c.getTile( i, j ).getEntityOnTile() == null ) {
					c.setEntityCoordinate( msg.getPlayer(), i, j );
					set = true;
					break;
				}
			}
			if ( set ) {
				break;
			}
		}

		if ( !receiver.containsKey( c.getPosition() ) ) {
			receiver.put( c.getPosition(), new ArrayList<INodeAddress>() );
		}
		messagePassing.send( new GameEnteredMessage( c, (ArrayList<INodeAddress>) receiver.get( c.getPosition() )
				.clone() ), origin );
		receiver.get( c.getPosition() ).add( origin );

	}

	public void receive( final GameEnteredMessage msg, final INodeAddress origin ) {
		if ( receiver.containsKey( msg.getChunk().getPosition() ) ) {
			receiver.get( msg.getChunk().getPosition() ).remove( origin );
		}
		worldAdapter.gameEntered( msg.getChunk() );
		local.put( msg.getChunk().getPosition(), msg.getLocalPlayer() );
	}

	public void receive( final EntityAddedMessage msg, final INodeAddress origin ) {
		TileCoordinate coordinate = Chunk.containingCunk( msg.getCoordinate() );
		if ( local.containsKey( coordinate ) ) {
			worldAdapter.getChunk( coordinate ).setEntityCoordinate( msg.getEntity(), msg.getCoordinate().getX(),
					msg.getCoordinate().getY() );
			local.get( coordinate ).add( msg.getOwner() );
		}
	}
}
