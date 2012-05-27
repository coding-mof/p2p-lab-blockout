package org.blockout.world;

import java.lang.reflect.InvocationTargetException;

import org.blockout.common.TileCoordinate;
import org.blockout.network.INodeAddress;
import org.blockout.network.message.IMessage;
import org.blockout.network.message.IMessagePassing;
import org.blockout.world.entity.Player;
import org.blockout.world.event.IEvent;
import org.blockout.world.event.PlayerMoveEvent;
import org.blockout.world.messeges.ChuckRequestMessage;
import org.blockout.world.messeges.ChunkDeliveryMessage;
import org.blockout.world.messeges.StateMessage;
import org.blockout.world.messeges.StopUpdatesMessage;
import org.blockout.world.state.IStateMachine;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DefaultChunkManagerTest {
	
	DefaultChunkManager 	manager;
	WorldAdapter 			worldAdapter;
	IStateMachine 			iStateMachine;
	IMessagePassing			messagePassing;
	
	@Before
	public void init(){
		worldAdapter = mock(WorldAdapter.class);
		iStateMachine = mock(IStateMachine.class);
		messagePassing = mock(IMessagePassing.class);
		manager = new DefaultChunkManager(iStateMachine, worldAdapter, messagePassing);
	}
	
	@Test
	public void receiveState() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		IEvent<?> event = mock(IEvent.class);
		INodeAddress origin = mock(INodeAddress.class);
		//commit
		IMessage msg = new StateMessage(event, StateMessage.COMMIT_MESSAGE);
		manager.receive(msg, origin);
		verify(iStateMachine).commitEvent(event);
		//push
		msg = new StateMessage(event, StateMessage.Push_MESSAGE);
		manager.receive(msg, origin);
		verify(iStateMachine).pushEvent(event);
		//rollback
		msg = new StateMessage(event, StateMessage.ROLLBAK_MESSAGE);
		manager.receive(msg, origin);
		verify(iStateMachine).rollbackEvent(event);
	}
	
	@Test
	public void ChunkUpdates() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		//setup
		Player p = mock(Player.class);
		INodeAddress a1 = mock(INodeAddress.class);
		INodeAddress a2 = mock(INodeAddress.class);
		TileCoordinate coord1 = new TileCoordinate(1, 1);
		TileCoordinate coord2 = new TileCoordinate(2, 3);
		TileCoordinate coord3 = new TileCoordinate(-4, 4);
		IEvent<?> e1 = mock(IEvent.class);
		stub(e1.getResponsibleTile()).toReturn(new TileCoordinate(coord1.getX()*Chunk.CHUNK_SIZE+1, coord1.getY()*Chunk.CHUNK_SIZE+4));
		IEvent<?> e2 = mock(IEvent.class);
		stub(e2.getResponsibleTile()).toReturn(new TileCoordinate(coord2.getX()*Chunk.CHUNK_SIZE+2, coord2.getY()*Chunk.CHUNK_SIZE+5));
		IEvent<?> e3 = mock(IEvent.class);
		stub(e3.getResponsibleTile()).toReturn(new TileCoordinate(coord3.getX()*Chunk.CHUNK_SIZE+3, coord3.getY()*Chunk.CHUNK_SIZE+6));
		IEvent<?> e4 = mock(IEvent.class);
		stub(e4.getResponsibleTile()).toReturn(new TileCoordinate(-4*Chunk.CHUNK_SIZE+3, -4*Chunk.CHUNK_SIZE+3));		
		IChunkGenerator generator = new BasicChunkGenerator();
		Chunk chunk1 =generator.generateChunk(coord1);
		Chunk chunk2 =generator.generateChunk(coord2);
		Chunk chunk3 =generator.generateChunk(coord3);
		stub(worldAdapter.getChunk(coord1)).toReturn(chunk1);
		stub(worldAdapter.getChunk(coord2)).toReturn(chunk2);
		stub(worldAdapter.getChunk(coord3)).toReturn(chunk3);
		
		
		
		//chunkRequests
		//coord1
		IMessage msg = new ChuckRequestMessage(coord1);
		manager.receive(msg, a1);
		verify(worldAdapter).getChunk(coord1);
		verify(messagePassing).send(new ChunkDeliveryMessage(chunk1), a1);
		//coord2
		msg = new ChuckRequestMessage(coord2);
		manager.receive(msg, a1);
		verify(worldAdapter).getChunk(coord2);
		verify(messagePassing).send(new ChunkDeliveryMessage(chunk2), a1);
		msg = new ChuckRequestMessage(coord2);
		manager.receive(msg, a2);
		verify(messagePassing).send(new ChunkDeliveryMessage(chunk2), a2);
		//coord3
		msg = new ChuckRequestMessage(coord3);
		manager.receive(msg, a2);
		verify(worldAdapter).getChunk(coord3);
		verify(messagePassing).send(new ChunkDeliveryMessage(chunk3), a2);
		
		//updates
		//push
		manager.eventPushed(e1);
		verify(iStateMachine).commitEvent(e1);
		verify(messagePassing).send(new StateMessage(e1, StateMessage.COMMIT_MESSAGE), a1);
		verify(messagePassing,never()).send(new StateMessage(e1, StateMessage.COMMIT_MESSAGE), a2);
		manager.eventPushed(e2);
		verify(iStateMachine).commitEvent(e2);
		verify(messagePassing).send(new StateMessage(e2, StateMessage.COMMIT_MESSAGE), a1);
		verify(messagePassing).send(new StateMessage(e2, StateMessage.COMMIT_MESSAGE), a2);
		manager.eventPushed(e3);
		verify(iStateMachine).commitEvent(e3);
		verify(messagePassing, never()).send(new StateMessage(e3, StateMessage.COMMIT_MESSAGE), a1);
		verify(messagePassing).send(new StateMessage(e3, StateMessage.COMMIT_MESSAGE), a2);
		manager.eventPushed(e4);
		verify(iStateMachine, never()).commitEvent(e4);
		//commit
		manager.eventCommitted(e1);
		verify(messagePassing, times(2)).send(new StateMessage(e1, StateMessage.COMMIT_MESSAGE), a1);
		verify(messagePassing,never()).send(new StateMessage(e1, StateMessage.COMMIT_MESSAGE), a2);
		manager.eventCommitted(e2);
		verify(messagePassing, times(2)).send(new StateMessage(e2, StateMessage.COMMIT_MESSAGE), a1);
		verify(messagePassing, times(2)).send(new StateMessage(e2, StateMessage.COMMIT_MESSAGE), a2);
		manager.eventCommitted(e3);
		verify(messagePassing, never()).send(new StateMessage(e3, StateMessage.COMMIT_MESSAGE), a1);
		verify(messagePassing, times(2)).send(new StateMessage(e3, StateMessage.COMMIT_MESSAGE), a2);
		//rollback
		manager.eventRolledBack(e1);
		verify(messagePassing).send(new StateMessage(e1, StateMessage.ROLLBAK_MESSAGE), a1);
		verify(messagePassing,never()).send(new StateMessage(e1, StateMessage.ROLLBAK_MESSAGE), a2);
		manager.eventRolledBack(e2);
		verify(messagePassing).send(new StateMessage(e2, StateMessage.ROLLBAK_MESSAGE), a1);
		verify(messagePassing).send(new StateMessage(e2, StateMessage.ROLLBAK_MESSAGE), a2);
		manager.eventRolledBack(e3);
		verify(messagePassing, never()).send(new StateMessage(e3, StateMessage.ROLLBAK_MESSAGE), a1);
		verify(messagePassing).send(new StateMessage(e3, StateMessage.ROLLBAK_MESSAGE), a2);
		
		//stopUpdate
		msg = new StopUpdatesMessage(coord2);
		manager.receive(msg, a1);
		manager.eventCommitted(e2);
		verify(messagePassing, times(2)).send(new StateMessage(e2, StateMessage.COMMIT_MESSAGE), a1);
		verify(messagePassing, times(3)).send(new StateMessage(e2, StateMessage.COMMIT_MESSAGE), a2);
	}
}
