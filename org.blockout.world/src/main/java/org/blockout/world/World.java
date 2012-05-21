package org.blockout.world;

import java.util.Hashtable;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Entity;
import org.blockout.world.entity.Player;

public class World implements IWorld, WorldAdapter {

	private TileCoordinate 						pos;
	private Hashtable<TileCoordinate, Chunk>	view;
	private Hashtable<TileCoordinate, Chunk> 	managedChunks;
	private IChunkManager						chunkManager;
	private IChunkGenerator						chunkGenerator;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tile getTile( final int x, final int y ) {
		int chunkx, chunky;
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
			return chunk.getTile(x, y);
		}
		if((chunk = managedChunks.get(new TileCoordinate(chunkx, chunky))) != null){
			return chunk.getTile(x, y);
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
		int chunkx,chunky;
		chunkx = coord.getX()/Chunk.CHUNK_SIZE;
		chunky = coord.getY()/Chunk.CHUNK_SIZE;
		if(!pos.equals(new TileCoordinate(chunkx, chunky))){
			view.get(pos).removeEntity(p);
			pos = new TileCoordinate(chunkx, chunky);
			
			//request new chunks for buffer
			for (int x = pos.getX()-1; x < pos.getX()+1; x++) {
				for (int y = pos.getX()-1; y < pos.getY(); y++) {
					if(!view.containsKey(new TileCoordinate(x, y))){
						if(!managedChunks.containsKey(new TileCoordinate(x, y))){
							chunkManager.requestChunk(new TileCoordinate(x, y));
						}else{
							view.put(new TileCoordinate(x, y), managedChunks.get(new TileCoordinate(x, y)));
						}
					}
				}
			}
		}
		view.get(pos).setEntityCoordinate(p, coord.getX(), coord.getY());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnityPosition(Entity e, TileCoordinate coord) {
		TileCoordinate tmp = findTile(e);
		if (tmp != null) {
			int x1 = tmp.getX() / Chunk.CHUNK_SIZE;
			int y1 = tmp.getY() / Chunk.CHUNK_SIZE;
			if(view.containsKey(new TileCoordinate(x1, y1))){
				view.get(new TileCoordinate(x1, y1)).removeEntity(e);
			}
			if(managedChunks.containsKey(new TileCoordinate(x1, y1))){
				managedChunks.get(new TileCoordinate(x1, y1)).removeEntity(e);
			}
		}
		int x2 = coord.getX() / Chunk.CHUNK_SIZE;
		int y2 = coord.getY() / Chunk.CHUNK_SIZE;
		if(view.containsKey(new TileCoordinate(x2, y2))){
			view.get(new TileCoordinate(x2, y2)).setEntityCoordinate(e, x2, y2);
		}
		if(managedChunks.containsKey(new TileCoordinate(x2, y2))){
			managedChunks.get(new TileCoordinate(x2, y2)).setEntityCoordinate(e, x2, y2);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void manageChunk(Chunk chunk) {
		if(!managedChunks.containsKey(chunk.getPosition())){
			managedChunks.put(chunk.getPosition(), chunk);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Chunk unmanageChunk(TileCoordinate coord) {
		return managedChunks.remove(coord);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Chunk getChunk(TileCoordinate coord) {
		Chunk c = managedChunks.get(coord);
		if(c == null){
			c = chunkGenerator.generateChunk(coord);
			managedChunks.put(coord, c);
		}
		return c;
	}

}
