package org.blockout.world;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Entity;
import org.blockout.world.entity.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;



public class TestWorld {
	
	World w;
	IChunkManager m;
	
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
		TileCoordinate coordinate = Chunk.containingCunk(w.findTile(p));
		verify(m, never()).requestChunk(coordinate);
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
	
	@Test
	public void movePlayerEntity(){
		Player p = mock(Player.class);
		w.init(p);
		TileCoordinate coordinate = w.findTile(p);
		w.setPlayerPosition(p, new TileCoordinate(5, 5));
		assertNull(w.getTile(coordinate.getX(), coordinate.getY()).getEntityOnTile());
		assertEquals(p, w.getTile(5, 5).getEntityOnTile());
		w.setEnityPosition(p, new TileCoordinate(7, 7));
		assertNull(w.getTile(5, 5).getEntityOnTile());
		assertEquals(p, w.getTile(7, 7).getEntityOnTile());
	}
	
	@Test
	public void removeEntity(){
		Player p = mock(Player.class);
		Entity e = mock(Entity.class);
		w.init(p);
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
		w.unmanageChunk(c.getPosition());
		assertNull(w.getTile(coordinate.getX(), coordinate.getY()));
		assertNotNull(w.getChunk(c.getPosition()));
	}

}
