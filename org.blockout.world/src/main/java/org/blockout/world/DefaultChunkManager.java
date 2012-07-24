package org.blockout.world;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.blockout.common.TileCoordinate;
import org.blockout.network.dht.Hash;
import org.blockout.network.dht.IHash;
import org.blockout.network.dht.WrappedRange;
import org.blockout.network.message.IMessage;
import org.blockout.network.reworked.ChordListener;
import org.blockout.network.reworked.IChordOverlay;
import org.blockout.world.entity.Player;
import org.blockout.world.event.AttackEvent;
import org.blockout.world.event.IEvent;
import org.blockout.world.messeges.ChuckRequestMessage;
import org.blockout.world.messeges.ChunkDeliveryMessage;
import org.blockout.world.messeges.ChunkEnteredMessage;
import org.blockout.world.messeges.EnterGameMessage;
import org.blockout.world.messeges.EntityAddedMessage;
import org.blockout.world.messeges.GameEnteredMessage;
import org.blockout.world.messeges.ManageMessage;
import org.blockout.world.messeges.StateMessage;
import org.blockout.world.messeges.StopUpdatesMessage;
import org.blockout.world.state.IStateMachine;
import org.blockout.world.state.IStateMachineListener;
import org.blockout.world.state.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Konstantin Ramig
 */
public class DefaultChunkManager implements IChunkManager, IStateMachineListener, ChordListener {

	private static final Logger									logger;
	static {
		logger = LoggerFactory.getLogger( DefaultChunkManager.class );
	}

	private IStateMachine										stateMachine;

	private final WorldAdapter									worldAdapter;

	private final IChordOverlay									chord;

	private final List<ChunkManagerListener>					listener;

	private final Hashtable<TileCoordinate, ArrayList<IHash>>	receiver;

	private final Hashtable<TileCoordinate, ArrayList<IHash>>	local;
	
	private final HashSet<IEvent> pushedEvents;

	public DefaultChunkManager(final WorldAdapter worldAdapter, final IChordOverlay chord) {
		receiver = new Hashtable<TileCoordinate, ArrayList<IHash>>();
		local = new Hashtable<TileCoordinate, ArrayList<IHash>>();
		this.worldAdapter = worldAdapter;
		this.worldAdapter.setManager( this );
		this.chord = chord;
		listener = new ArrayList<ChunkManagerListener>();
		chord.addChordListener( this );
		
		pushedEvents = new HashSet<IEvent>();
	}

	@Override
	public void init( final IStateMachine stateMachine ) {
		this.stateMachine = stateMachine;
	}

	@Override
	public void eventCommitted( final IEvent<?> event ) {
			if(pushedEvents.contains(event)){
				pushedEvents.remove(event);
			}
		logger.debug( "Event committed " + event );
		TileCoordinate coordinate = Chunk.containingCunk( event.getResponsibleTile() );
		if ( chord.getResponsibility().contains(new Hash(coordinate))) {
			
			if(receiver.containsKey( coordinate ) ){
				
				clean(receiver.get(coordinate));
				
				for ( IHash address : receiver.get( coordinate ) ) {
					logger.debug( "Sending event " + event + " to " + address );
					chord.sendMessage( new StateMessage( event, StateMessage.Type.COMMIT_MESSAGE ), address );
				}
			}
			
			fireChunkUpdate( new StateMessage( event, StateMessage.Type.COMMIT_MESSAGE ) );
		}
	}

	@Override
	public void eventPushed(final IEvent<?> event) {
		boolean send = true;
			if(pushedEvents.contains(event)){
				send = false;
			}else{
				pushedEvents.add(event);
			}
		
		
		logger.debug("Event pushed " + event);
		TileCoordinate coordinate = Chunk.containingCunk(event
				.getResponsibleTile());

		// push&commit event if within own responsibility
		if (chord.getResponsibility().contains(new Hash(coordinate))) {

			// send push to anyone receiving updates for the chunk
			if (send && receiver.containsKey(coordinate)) {
				
				clean(receiver.get(coordinate));
				
				for (IHash address : receiver.get(coordinate)) {
					chord.sendMessage(new StateMessage(event,
							StateMessage.Type.PUSH_MESSAGE), address);
				}
			}
			//commit
			stateMachine.commitEvent(event);
		} else {
			
			chord.sendMessage(new StateMessage(event,
					StateMessage.Type.PUSH_MESSAGE), new Hash(coordinate));
		}

		// send pushed event to all players local connected players receiving updates for the chunk
		if (local.containsKey(coordinate)) {
			
			clean(local.get(coordinate));
			
			for (IHash address : local.get(coordinate)) {
				chord.sendMessage(new StateMessage(event,
						StateMessage.Type.PUSH_MESSAGE), address);
			}
		}

	}

	@Override
	public void eventRolledBack( final IEvent<?> event ) {
		
			if(pushedEvents.contains(event)){
				pushedEvents.remove(event);
			}
		
		TileCoordinate coordinate = Chunk.containingCunk( event.getResponsibleTile() );
		if ( receiver.containsKey( coordinate ) ) {
			
			clean(receiver.get(coordinate));
			
			for ( IHash address : receiver.get( coordinate ) ) {
				chord.sendMessage( new StateMessage( event, StateMessage.Type.ROLLBAK_MESSAGE ), address );
			}
		}
		if ( local.contains( coordinate ) ) {
			
			clean(local.get(coordinate));
			
			for ( IHash address : local.get( coordinate ) ) {
				chord.sendMessage( new StateMessage( event, StateMessage.Type.ROLLBAK_MESSAGE ), address );
			}
		}
	}

	@Override
	public void requestChunk( final TileCoordinate position ) {
		if ( receiver.containsKey( position ) ) {
			local.put( position, receiver.get( position ) );
			worldAdapter.responseChunk( worldAdapter.getChunk( position ) );

			for ( IHash address : local.get( position ) ) {
				chord.sendMessage( new ChunkEnteredMessage( position ), address );
			}
		} else {
			local.put( position, new ArrayList<IHash>() );
			chord.sendMessage( new ChuckRequestMessage( position ), new Hash( position ) );
		}

	}

	@Override
	public void stopUpdating( final TileCoordinate position ) {
		chord.sendMessage( new StopUpdatesMessage( position ), new Hash( position ) );
		if ( local.containsKey( position ) ) {
			for ( IHash address : local.get( position ) ) {
				chord.sendMessage( new StopUpdatesMessage( position ), address );
			}
		}
		local.remove( position );
	}

	@Override
	public void enterGame( final Player player ) {
		chord.sendMessage( new EnterGameMessage( player ), new Hash( new TileCoordinate( 0, 0 ) ) );
	}

	private void receive( final StateMessage msg, final IHash origin ) {
		logger.debug( "Received message " + msg + " from " + origin );
		switch ( msg.getType() ) {
			case ROLLBAK_MESSAGE:				
				stateMachine.rollbackEvent( msg.getEvent() );
				break;
			case COMMIT_MESSAGE:
				stateMachine.commitEvent( msg.getEvent() );
				break;
			case PUSH_MESSAGE:
					if(pushedEvents.contains(msg.getEvent())){
						return;
					}else {
						pushedEvents.add(msg.getEvent());
					}
				ValidationResult result = stateMachine.pushEvent( msg.getEvent() );
				if ( result == ValidationResult.Invalid ) {
					eventRolledBack( msg.getEvent() );
				}
				break;
			default:
				break;
		}
	}

	private void receive( final ChuckRequestMessage msg, final IHash origin ) {
		Chunk c = worldAdapter.getChunk( msg.getCoordinate() );
		if ( !receiver.containsKey( c.getPosition() ) ) {
			logger.debug( "Clearing receivers for " + c.getPosition() );
			receiver.put( c.getPosition(), new ArrayList<IHash>() );
		}
		
		clean(receiver.get(c.getPosition()));
		
		fireChunkUpdate( new ChunkDeliveryMessage( c, null ) );
		chord.sendMessage(
				new ChunkDeliveryMessage( c, (ArrayList<IHash>) receiver.get( msg.getCoordinate() ).clone() ), origin );
		//prevent sending updates to one self
		if(!origin.equals(chord.getLocalId())){
			receiver.get( c.getPosition() ).add( origin );
		}
	}

	private void receive( final ChunkDeliveryMessage msg, final IHash origin ) {
		Chunk c = msg.getChunk();
		if ( local.containsKey( c.getPosition() ) ) {
			ArrayList<IHash> localPlayers = msg.getLocalPlayers();
			clean(localPlayers);
			for ( IHash h : localPlayers ) {
				if ( chord.getResponsibility().contains( h ) ) {
					System.err.println( "Added local id to receivers" );
					new Throwable().printStackTrace();
					System.exit( -1 );
				}
			}
			logger.debug( "Adding receiver " + localPlayers + " for " + c.getPosition() );
			receiver.put( c.getPosition(), localPlayers );

			worldAdapter.responseChunk( c );

			//telling players acting within this chunk to add this id to lacal connected players
			for ( IHash address : local.get( c.getPosition() ) ) {
				chord.sendMessage( new ChunkEnteredMessage( c.getPosition() ), address );
			}
		}

	}

	private void receive( final ChunkEnteredMessage msg, final IHash origin ) {
		if ( local.containsKey( msg.getCoordinate() ) ) {
			
			clean(local.get(msg.getCoordinate()));
			
			local.get( msg.getCoordinate() ).add( origin );
		}
	}

	private void receive( final StopUpdatesMessage msg, final IHash origin ) {
		if ( receiver.containsKey( msg.getCoordinate() ) ) {
			receiver.get( msg.getCoordinate() ).remove( origin );
		}
		if ( local.contains( msg.getCoordinate() ) ) {
			local.get( msg.getCoordinate() ).remove( origin );
		}
	}

	public void receive( final ManageMessage msg, final IHash origin ) {

		ArrayList<Chunk> chunks = msg.getChunks();
		ArrayList<ArrayList<IHash>> addresses = msg.getReceivers();
		synchronized ( receiver ) {
			synchronized ( worldAdapter ) {
				for ( int i = 0; i < chunks.size(); i++ ) {					
					ArrayList<IHash> value = addresses.get( i );
					for ( IHash h : value ) {
						if ( chord.getResponsibility().contains( h ) ) {
							System.err.println( "Added local id to receivers" );
							new Throwable().printStackTrace();
							System.exit( -1 );
						}
					}
					receiver.put( chunks.get( i ).getPosition(), value );
					worldAdapter.manageChunk( chunks.get( i ) );
				}
			}
		}

	}

	public void receive(final EnterGameMessage msg, final IHash origin) {
		Chunk c = worldAdapter.getChunk(new TileCoordinate(0, 0));

		// TODO better player placement
		TileCoordinate coord;
		synchronized (c) {
			coord = c.findFreeTile();
			c.setEntityCoordinate(msg.getPlayer(), coord.getX(), coord.getY());
		}
		fireChunkUpdate(new EntityAddedMessage(msg.getPlayer(), coord, origin));

		if (!receiver.containsKey(c.getPosition())) {
			logger.debug("Clearing receivers for " + c.getPosition());
			receiver.put(c.getPosition(), new ArrayList<IHash>());
		}
		chord.sendMessage(new GameEnteredMessage(c, (ArrayList<IHash>) receiver
				.get(c.getPosition()).clone()), origin);

		// prevent sending updates to oneself
		if (!origin.equals(chord.getLocalId())) {
			receiver.get(c.getPosition()).add(origin);
		}

		logger.debug("Player: " + msg.getPlayer().getName() + " [" + origin
				+ "] joined the Game");
	}

	public void receive( final GameEnteredMessage msg, final IHash origin ) {
		worldAdapter.gameEntered( msg.getChunk() );
		local.put( msg.getChunk().getPosition(), msg.getLocalPlayer() );
	}

	public void receive( final EntityAddedMessage msg, final IHash origin ) {
		TileCoordinate coordinate = Chunk.containingCunk( msg.getCoordinate() );
		if ( local.containsKey( coordinate ) ) {
			worldAdapter.getChunk( coordinate ).setEntityCoordinate( msg.getEntity(), msg.getCoordinate().getX(),
					msg.getCoordinate().getY() );
			local.get( coordinate ).add( msg.getOwner() );
		}
	}

	@Override
	public void responsibilityChanged( final IChordOverlay chord, final WrappedRange<IHash> from,
			final WrappedRange<IHash> to ) {
		synchronized ( receiver ) {
			synchronized ( worldAdapter ) {
				ManageMessage msg = new ManageMessage();
				for ( TileCoordinate coordinate : receiver.keySet() ) {
					if ( !to.contains( new Hash( coordinate ) ) ) {
						if ( local.containsKey( coordinate ) ) {
							receiver.get( coordinate ).add( chord.getLocalId() );
						}
						msg.add( worldAdapter.unmanageChunk( coordinate ), receiver.get( coordinate ) );
					}
				}

				if ( msg.getChunks().size() > 0 ) {
					chord.sendMessage( msg, new Hash( msg.getChunks().get( 0 ).getPosition() ) );

					for ( Chunk c : msg.getChunks() ) {
						receiver.remove( c.getPosition() );
					}
				}

			}
		}

	}

	@Override
	public void receivedMessage( final IChordOverlay chord, final Object message, final IHash senderId ) {
		if ( message instanceof StateMessage ) {
			receive( (StateMessage) message, senderId );
		} else if ( message instanceof ChuckRequestMessage ) {
			receive( (ChuckRequestMessage) message, senderId );
		} else if ( message instanceof ChunkDeliveryMessage ) {
			receive( (ChunkDeliveryMessage) message, senderId );
		} else if ( message instanceof ChunkEnteredMessage ) {
			receive( (ChunkEnteredMessage) message, senderId );
		} else if ( message instanceof StopUpdatesMessage ) {
			receive( (StopUpdatesMessage) message, senderId );
		} else if ( message instanceof GameEnteredMessage ) {
			receive( (GameEnteredMessage) message, senderId );
		} else if ( message instanceof EnterGameMessage ) {
			receive( (EnterGameMessage) message, senderId );
		} else if ( message instanceof EntityAddedMessage ) {
			receive( (EntityAddedMessage) message, senderId );
		} else if ( message instanceof ManageMessage ) {
			receive( (ManageMessage) message, senderId );
		}
	}

	@Override
	public void addListener( final ChunkManagerListener listener ) {
		this.listener.add( listener );
	}

	@Override
	public void removeListener( final ChunkManagerListener listener ) {
		this.listener.remove( listener );
	}

	private void fireChunkUpdate( final IMessage message ) {
		synchronized ( listener ) {
			for ( ChunkManagerListener listener : this.listener ) {
				listener.chunkUpdated( message );
			}
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

    //method for cleaning list of receivers to prevent sending updates to one self
    private void clean(ArrayList<IHash> adresses){
    	synchronized (adresses) {
    		for (Iterator<IHash> iterator = adresses.iterator(); iterator.hasNext();) {
    			IHash iHash = iterator.next();
    			if (chord.getResponsibility().contains(iHash)) {
    				iterator.remove();
    			}
    		}
		}    	
    }
}
