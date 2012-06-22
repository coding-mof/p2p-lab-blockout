package org.blockout.world;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Actor;
import org.blockout.world.entity.Entity;
import org.blockout.world.entity.Player;
import org.blockout.world.event.AttackEvent;
import org.blockout.world.event.IEvent;
import org.blockout.world.state.ValidationResult;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;



public class TestWorld {
	
	World w;
	IChunkManager m;
	
	private static final Logger	logger;
	static {
		logger = LoggerFactory.getLogger( World.class );
	}
	
	@Before
	public void setUp(){
		w = new World();
		m = mock(IChunkManager.class);
		w.setManager(m);
	}
	
	
	@Test
	public void init(){
		
		Player p = mock(Player.class);
		w.init(p);
		verify(m).enterGame(p);
		
		Chunk c = w.getChunk(new TileCoordinate(0, 0));
		c.setEntityCoordinate(p, 0, 0);
		
		w.gameEntered(c);
		TileCoordinate coordinate = c.getPosition();
		verify(m).requestChunk(new TileCoordinate(coordinate.getX(), coordinate.getY()+1));
		verify(m).requestChunk(new TileCoordinate(coordinate.getX(), coordinate.getY()-1));
		verify(m).requestChunk(new TileCoordinate(coordinate.getX()+1, coordinate.getY()+1));
		verify(m).requestChunk(new TileCoordinate(coordinate.getX()+1, coordinate.getY()-1));
		verify(m).requestChunk(new TileCoordinate(coordinate.getX()-1, coordinate.getY()+1));
		verify(m).requestChunk(new TileCoordinate(coordinate.getX()-1, coordinate.getY()-1));
		verify(m).requestChunk(new TileCoordinate(coordinate.getX()+1, coordinate.getY()));
		verify(m).requestChunk(new TileCoordinate(coordinate.getX()-1, coordinate.getY()));
		coordinate = w.findTile(p);
		assertEquals(p, w.getTile(coordinate.getX(), coordinate.getY()).getEntityOnTile());
		assertEquals(coordinate, w.findTile(p));
	}
	
	private Player initW(){
		Player p = mock(Player.class);
		Chunk c = w.getChunk(new TileCoordinate(0, 0));
		c.setEntityCoordinate(p, 0, 0);
		w.gameEntered(c);
		return p;
	}
	
	@Test
	public void movePlayerEntity(){
		Player p = initW();
		
		TileCoordinate coordinate = w.findTile(p);
		w.setPlayerPosition(p, new TileCoordinate(5, 5));
		assertNull(w.getTile(coordinate.getX(), coordinate.getY()).getEntityOnTile());
		assertEquals(p, w.getTile(5, 5).getEntityOnTile());
		assertEquals(new TileCoordinate(5, 5), w.findTile(p));
		w.setEnityPosition(p, new TileCoordinate(7, 7));
		assertNull(w.getTile(5, 5).getEntityOnTile());
		assertEquals(p, w.getTile(7, 7).getEntityOnTile());
	}
	
	@Test
	public void removeEntity(){
		Player p = initW();
		Entity e = mock(Entity.class);
		
		TileCoordinate pt = w.findTile(p);
		TileCoordinate et = new TileCoordinate(pt.getX()+1, pt.getX()+1);
		w.setEnityPosition(e, et);
		w.removeEntity(p);
		w.removeEntity(e);
		assertNotNull(w.getTile(pt.getX(), pt.getY()));
		assertNotNull(w.getTile(et.getX(), et.getY()));		
	}
	
	@Test
	public void ChunkGetManageUnmanage(){
		IChunkGenerator g = new BasicChunkGenerator();
		Chunk c = g.generateChunk(new TileCoordinate(10, 13));
		TileCoordinate coordinate = new TileCoordinate(10*Chunk.CHUNK_SIZE+5, 13*Chunk.CHUNK_SIZE+3);
		assertNull(w.getTile(coordinate.getX(), coordinate.getY()));
		w.manageChunk(c);
		assertEquals(c.getTile(coordinate.getX(), coordinate.getY()), w.getTile(coordinate.getX(), coordinate.getY()));
		assertEquals(c, w.unmanageChunk(c.getPosition()));
		assertNull(w.getTile(coordinate.getX(), coordinate.getY()));
		assertNotNull(w.getChunk(c.getPosition()));
		assertNotSame(c, w.getChunk(c.getPosition()));
	}
	
	@Test
	public void findTile(){
		
		Player p = initW();
		for(int i = -2; i< 3; i++){
			for(int j = -2; j< 3; j++){
				w.getChunk(new TileCoordinate(i, j));
			}
		}
		
		TileCoordinate playerOld = w.findTile(p);
		assertEquals(p, w.getTile(playerOld.getX(), playerOld.getY()).getEntityOnTile());
		TileCoordinate playerNew = new TileCoordinate(-(Chunk.CHUNK_SIZE+3), -(Chunk.CHUNK_SIZE+7));
		w.setPlayerPosition(p, playerNew);
		assertEquals(playerNew, w.findTile(p));
		assertEquals(p, w.getTile(playerNew.getX(), playerNew.getY()).getEntityOnTile());
		assertNull(w.getTile(playerOld.getX(), playerOld.getY()).getEntityOnTile());
	}
}
