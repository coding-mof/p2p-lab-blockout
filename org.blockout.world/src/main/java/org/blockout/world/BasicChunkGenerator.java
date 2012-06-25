package org.blockout.world;

import java.util.Random;

import org.blockout.common.TileCoordinate;
import org.blockout.engine.SpriteMapping;
import org.blockout.engine.SpriteType;
import org.blockout.world.entity.Crate;
import org.blockout.world.entity.Zombie;

/**
 * 
 * @author Konstantin Ramig
 */
public class BasicChunkGenerator implements IChunkGenerator {

	private final SpriteMapping	mapping	= new SpriteMapping();

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
		Random r = new Random();

		int mobcount = r.nextInt( 20 ) + 1;
		int chestCount = r.nextInt( 10 ) + 1;
		for ( int i = 0; i < mobcount; i++ ) {
			boolean finished = false;
			while ( !finished ) {
				int x = r.nextInt( Chunk.CHUNK_SIZE );
				int y = r.nextInt( Chunk.CHUNK_SIZE );

				if ( tiles[x][y].getHeight() == Tile.GROUND_HEIGHT && tiles[x][y].getEntityOnTile() == null ) {
					tiles[x][y] = new Tile( tiles[x][y].getTileType(), new Zombie( 5 ) );
					finished = true;
				}

			}
		}

		for ( int i = 0; i < chestCount; i++ ) {
			boolean finished = false;
			while ( !finished ) {
				int x = r.nextInt( Chunk.CHUNK_SIZE );
				int y = r.nextInt( Chunk.CHUNK_SIZE );

				if ( tiles[x][y].getHeight() == Tile.GROUND_HEIGHT && tiles[x][y].getEntityOnTile() == null ) {
					tiles[x][y] = new Tile( tiles[x][y].getTileType(), new Crate() );
					finished = true;
				}

			}
		}

		return new Chunk( pos, tiles );
	}

	@Override
	public Chunk generateChunk( final TileCoordinate coordinate ) {
		return generateBasicChunk( coordinate, Chunk.CHUNK_SIZE );
	}

}
