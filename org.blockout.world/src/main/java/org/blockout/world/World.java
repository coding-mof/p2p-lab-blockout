package org.blockout.world;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Entity;
import org.blockout.world.entity.Player;

public class World implements IWorld {

	private Chunk			current;
	private IChunkManager	chunkManager;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tile getTile( final int x, final int y ) {
		int chunkx, chunky, tilex, tiley;
		chunkx = x / Chunk.CHUNK_SIZE;
		chunky = y / Chunk.CHUNK_SIZE;
		tilex = x % Chunk.CHUNK_SIZE;
		tiley = y % Chunk.CHUNK_SIZE;
		if ( tilex < 0 ) {
			chunkx = chunkx - 1;
		}
		if ( tiley < 0 ) {
			chunky = chunky - 1;
		}
		if ( current.getX() == chunkx && current.getY() == chunky ) {
			return current.getTile( tilex, tiley );
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileCoordinate findTile( final Entity entity ) {
		int x = 0;
		int y = 0;
		// TODO: seems to me quiet inefficient since the worst case
		// would be 48x48 = 2304 iterations
		for ( int i = 0; i < Chunk.CHUNK_SIZE; i++ ) {
			for ( int j = 0; j < Chunk.CHUNK_SIZE; j++ ) {
				if ( entity.equals( current.getTile( i, j ) ) ) {
					x = current.getX() * Chunk.CHUNK_SIZE + i;
					y = current.getY() * Chunk.CHUNK_SIZE + j;
					return new TileCoordinate( x, y );
				}
			}
		}
		return null;
	}

	@Override
	public void setPlayerPosition( final Player p, final TileCoordinate coord ) {
		// TODO Auto-generated method stub

	}

}
