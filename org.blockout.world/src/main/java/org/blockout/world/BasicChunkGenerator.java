package org.blockout.world;

import java.io.File;
import java.util.Random;

import org.blockout.common.TileCoordinate;
import org.blockout.engine.SpriteMapping;
import org.blockout.engine.SpriteType;
import org.blockout.world.entity.Crate;
import org.blockout.world.entity.Entity;
import org.blockout.world.entity.Zombie;

/**
 * 
 * @author Konstantin Ramig
 */
public class BasicChunkGenerator implements IChunkGenerator {

	private final SpriteMapping	mapping	= new SpriteMapping();
	
	private final IChunkLoader loader;
	
	private final Random r;
	
	
	public BasicChunkGenerator(IChunkLoader loader) {
		this.loader = loader;
		r= new Random();
	}
	
	public BasicChunkGenerator() {
		loader = null;
		r= new Random();
	}
	

	/**
	 * generates basic Chunk without any Entities or walls on it
	 * 
	 * @param pos
	 *            position of the Chunk in the World
	 * @param size
	 *            size of the Tilematrix
	 * @return an Chunk with the given coordinates an a Tilematrix sizeXsize
	 */
	public Chunk generateBasicChunk( final TileCoordinate pos, final int size ) {
		Tile[][] tiles = new Tile[size][size];
		for ( int i = 0; i < size; i++ ) {
			for ( int j = 0; j < size; j++ ) {
				tiles[i][j] = new Tile( mapping.getSpriteId( SpriteType.stoneground ) );
			}
		}
		
		Chunk c = new Chunk( pos, tiles );
		placeEntitys(c);
		
		return c;
	}
	
	private void placeEntitys(Chunk c){
		
		int posx,posy;
		posx = c.getX()*Chunk.CHUNK_SIZE;
		posy = c.getY()*Chunk.CHUNK_SIZE;
		
		

		int mobcount = r.nextInt( 20 ) + 1;
		int chestCount = r.nextInt( 10 ) + 1;
		for ( int i = 0; i < mobcount; i++ ) {
			boolean finished = false;
			while ( !finished ) {
				int x = r.nextInt( Chunk.CHUNK_SIZE );
				int y = r.nextInt( Chunk.CHUNK_SIZE );
				
								
				if ( c.getTile(x, y).getHeight() == Tile.GROUND_HEIGHT && c.getTile(x, y).getEntityOnTile() == null ) {
					c.setEntityCoordinate(getRandomMob(), x+posx, y+posy);
					finished = true;
				}

			}
		}

		for ( int i = 0; i < chestCount; i++ ) {
			boolean finished = false;
			while ( !finished ) {
				int x = r.nextInt( Chunk.CHUNK_SIZE );
				int y = r.nextInt( Chunk.CHUNK_SIZE );

				if ( c.getTile(x, y).getHeight() == Tile.GROUND_HEIGHT && c.getTile(x, y).getEntityOnTile() == null ) {
					c.setEntityCoordinate(new Crate(), x+posx, y+posy);
					finished = true;
				}

			}
		}
	}

	private Entity getRandomMob() {
		// TODO Auto-generated method stub
		return new Zombie(5);
	}

	@Override
	public Chunk generateChunk( final TileCoordinate coordinate ) {
		if(loader == null){
			return generateBasicChunk( coordinate, Chunk.CHUNK_SIZE );
		}else{
			return predefinedChunk(coordinate);
		}
	}

	private Chunk predefinedChunk(final TileCoordinate coordinate) {
		synchronized (this) {

			File[] files = loader.getMaps();
			int file = r.nextInt(files.length);
			Chunk c;

			try {
				c = loader.loadChunk(coordinate, files[file].getPath());

			} catch (Exception e) {
				e.printStackTrace();
				c = generateBasicChunk(coordinate, Chunk.CHUNK_SIZE);
			}

			return c;
		}
	}

}
