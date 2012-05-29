package org.blockout.world;

import java.util.ArrayList;
import java.util.Hashtable;
import javax.inject.Named;
import org.blockout.common.TileCoordinate;
import org.blockout.network.discovery.INodeAddress;
import org.blockout.network.dht.Hash;
import org.blockout.network.message.IMessagePassing;
import org.blockout.network.message.MessageReceiver;
import org.blockout.world.event.IEvent;
import org.blockout.world.messeges.*;
import org.blockout.world.state.IStateMachine;
import org.blockout.world.state.IStateMachineListener;

/**
 * 
 * @author Konstantin Ramig
 */
@Named
public class DefaultChunkManager extends MessageReceiver implements IChunkManager, IStateMachineListener {

	private IStateMachine										stateMachine;
	
	private WorldAdapter 										worldAdapter;
	
	private IMessagePassing										messegePassing;
	
	private Hashtable<TileCoordinate, ArrayList<INodeAddress>> 	receiver;

	
	
	public DefaultChunkManager(IStateMachine stateMachine,
			WorldAdapter worldAdapter, IMessagePassing network) {
		super();
		this.stateMachine = stateMachine;
		this.worldAdapter = worldAdapter;
		this.messegePassing = network;
		receiver = new Hashtable<TileCoordinate, ArrayList<INodeAddress>>();
	}

	@Override
	public void init( final IStateMachine stateMachine ) {
		this.stateMachine = stateMachine;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void eventCommitted(final IEvent<?> event) {
		TileCoordinate coordinate = Chunk.containingCunk(event
				.getResponsibleTile());
		if (receiver.containsKey(coordinate)) {
			for (INodeAddress address : receiver.get(coordinate)) {
				messegePassing.send(new StateMessage(event, StateMessage.COMMIT_MESSAGE),
						address);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void eventPushed(final IEvent<?> event) {
		TileCoordinate coordinate = Chunk.containingCunk(event
				.getResponsibleTile());
		if (receiver.containsKey(coordinate)) {
			stateMachine.commitEvent(event);
			eventCommitted(event);
		} else {
			messegePassing.send(new StateMessage(event,StateMessage.Push_MESSAGE), new Hash(coordinate));
		}

		// TODO add local connections
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void eventRolledBack(final IEvent<?> event) {
		TileCoordinate coordinate = Chunk.containingCunk(event
				.getResponsibleTile());
		if (receiver.containsKey(coordinate)) {
			for (INodeAddress address : receiver.get(coordinate)) {
				messegePassing.send(
						new StateMessage(event, StateMessage.ROLLBAK_MESSAGE),
						address);
			}
		}
		
		//TODO add local connections
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestChunk( final TileCoordinate position ) {
		messegePassing.send(new ChuckRequestMessage(position), new Hash(position));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stopUpdating(TileCoordinate position) {
		messegePassing.send(new StopUpdatesMessage(position), new Hash(position));
	}
	
	public void  receive(StateMessage msg, INodeAddress origin){
		switch (msg.getType()) {
		case StateMessage.ROLLBAK_MESSAGE:
			stateMachine.rollbackEvent(msg.getEvent());
			break;
		case StateMessage.COMMIT_MESSAGE:
			stateMachine.commitEvent(msg.getEvent());
			break;
		case StateMessage.Push_MESSAGE:
			stateMachine.pushEvent(msg.getEvent());
			break;
		default:
			break;
		}
	}
	
	public void  receive(ChuckRequestMessage msg, INodeAddress origin) {
		Chunk c =worldAdapter.getChunk(msg.getCoordinate());
		if(!receiver.containsKey(c.getPosition())){
			receiver.put(c.getPosition(), new ArrayList<INodeAddress>());
		}
		receiver.get(c.getPosition()).add(origin);
		messegePassing.send(new ChunkDeliveryMessage(c), origin);
	}
	
	public void  receive(ChunkDeliveryMessage msg, INodeAddress origin) {
		Chunk c =msg.getChunk();
		if(receiver.containsKey(c.getPosition())){
			receiver.get(c.getPosition()).remove(origin);
		}
		worldAdapter.responseChunk(c);
	}
	
	public void  receive(StopUpdatesMessage msg, INodeAddress origin) {
		if(receiver.containsKey(msg.getCoordinate())){
			receiver.get(msg.getCoordinate()).remove(origin);
		}
		//TODO remove from local connections
	}

}
