package org.blockout.world;

import java.util.Hashtable;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Entity;
import org.blockout.world.entity.Player;

public class World implements IWorld {

	private TileCoordinate 						pos;
	private Hashtable<TileCoordinate, Chunk>	view;
	private Hashtable<TileCoordinate, Chunk> 	managedChunks;
	private IChunkManager						chunkManager;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tile getTile( final int x, final int y ) {
		int chunkx, chunky, tilex, tiley;
		chunkx = x / Chunk.CHUNK_SIZE;
		chunky = y / Chunk.CHUNK_SIZE;
		if ( x < 0 ) {
			chunkx = chunkx - 1;
		}
		if ( y < 0 ) {
			chunky = chunky - 1;
		}
		Chunk chunk;
		if((chunk = view.get(new TileCoordinate(chunkx, chunky))) != null){
			chunk.getTile(x, y);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileCoordinate findTile( final Entity entity ) {
		TileCoordinate coordinate = null;
		for (Chunk c : view.values()) {
			if((coordinate = c.getEntityCoordinate(entity)) != null){
				return coordinate;
			}
		}
		for (Chunk c : managedChunks.values()) {
			if((coordinate = c.getEntityCoordinate(entity)) != null){
				return coordinate;
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPlayerPosition( final Player p, final TileCoordinate coord ) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnityPosition(Entity e, TileCoordinate coord) {
		// TODO Auto-generated method stub
		
	}

}
