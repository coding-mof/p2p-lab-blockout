package org.blockout.world;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javassist.bytecode.ByteArray;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Entity;
import org.blockout.world.entity.Player;
import org.blockout.world.entity.Zombie;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TestChunk {

	protected Chunk chunk;
	
	@Before
	public void setUp(){
		BasicChunkGenerator chunkGenerator = new BasicChunkGenerator();
		chunk = chunkGenerator.generateBasicChunk(new TileCoordinate(0, 0), Chunk.CHUNK_SIZE);
	}
	
	
	@Test
	public void getEntityCoordinate (){
		Entity e = Mockito.mock(Entity.class);
		assertNull(chunk.getEntityCoordinate(e));
		chunk.setEntityCoordinate(e, 1, 1);
		assertEquals(new TileCoordinate(1, 1), chunk.getEntityCoordinate(e));
	}
	
	
	@Test
	public void moveEntity(){
		Entity e = Mockito.mock(Entity.class);
		chunk.setEntityCoordinate(e, 0, 0);
		assertEquals(e, chunk.getTile(0, 0).getEntityOnTile());
		assertEquals(new TileCoordinate(0, 0), chunk.getEntityCoordinate(e));
		int type = chunk.getTile(1, 1).getTileType();
		chunk.setEntityCoordinate(e, 1, 1);
		assertEquals(e, chunk.getTile(1, 1).getEntityOnTile());
		assertEquals(new TileCoordinate(1, 1), chunk.getEntityCoordinate(e));
		chunk.setEntityCoordinate(e, 0, 1);
		assertEquals(type, chunk.getTile(1, 1).getTileType());
	}
	
	@Test
	public void removeEntity(){
		Entity e = Mockito.mock(Entity.class);
		chunk.setEntityCoordinate(e, 50, 5);
		chunk.removeEntity(e);
		assertNull(chunk.getEntityCoordinate(e));
		assertNull(chunk.getTile(50, 5).getEntityOnTile());
	}
	
	@Test
	public void serielize() throws IOException, ClassNotFoundException{
		Zombie e = new Zombie(5);
		chunk.setEntityCoordinate(e, 0, 0);
		ByteArrayOutputStream chars = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(chars);
		out.writeObject(chunk);
		ByteArrayInputStream charsIn = new ByteArrayInputStream(chars.toByteArray());
		ObjectInputStream in = new ObjectInputStream(charsIn);
		Chunk test = (Chunk)in.readObject();
		for (int i = 0; i < Chunk.CHUNK_SIZE; i++) {
			for (int j = 0; j < Chunk.CHUNK_SIZE; j++) {
				assertEquals(chunk.getTile(i, j).getEntityOnTile(), test.getTile(i, j).getEntityOnTile());
				assertEquals(chunk.getTile(i, j).getHeight(), test.getTile(i, j).getHeight());
				assertEquals(chunk.getTile(i, j).getTileType(), test.getTile(i, j).getTileType());
			}
		}
		assertEquals(chunk.getEntityCoordinate(e), test.getEntityCoordinate(e));
	}
}
