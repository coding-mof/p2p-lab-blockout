package org.blockout.world;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import javax.inject.Named;
import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Entity;
import org.blockout.world.entity.Player;

/**
 * Implementation of the {@link IWorld} interface with optional {@link WorldAdapter}
 * 
 * This implementation is based on {@link Chunk}s
 * 
 * @author key3
 *
 */
@Named
public class World implements IWorld, WorldAdapter {

	private TileCoordinate 						pos;
	private Hashtable<TileCoordinate, Chunk>	view;
	private Hashtable<TileCoordinate, Chunk> 	managedChunks;
	private IChunkManager						chunkManager;
	private IChunkGenerator						chunkGenerator;
	
	public World() {
		view = new Hashtable<TileCoordinate, Chunk>();
		managedChunks = new Hashtable<TileCoordinate, Chunk>();
		chunkGenerator = new BasicChunkGenerator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tile getTile(final int x, final int y) {
		TileCoordinate coordinate = Chunk.containingCunk(new TileCoordinate(x, y));
		Chunk chunk;
		if ((chunk = view.get(coordinate)) != null) {
			return chunk.getTile(x, y);
		}
		if ((chunk = managedChunks.get(coordinate)) != null) {
			return chunk.getTile(x, y);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileCoordinate findTile(final Entity entity) {
		TileCoordinate coordinate = null;
		for (Chunk c : view.values()) {
			if ((coordinate = c.getEntityCoordinate(entity)) != null) {
				return coordinate;
			}
		}
		for (Chunk c : managedChunks.values()) {
			if ((coordinate = c.getEntityCoordinate(entity)) != null) {
				return coordinate;
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPlayerPosition(final Player p, final TileCoordinate coord) {
		TileCoordinate newPos = Chunk.containingCunk(coord);
		if (!pos.equals(newPos)) {
			view.get(pos).removeEntity(p);
			pos = newPos;
			cleanView();
			updateView();
		}
		view.get(pos).setEntityCoordinate(p, coord.getX(), coord.getY());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnityPosition(final Entity e, final TileCoordinate coord) {
		TileCoordinate tmp = findTile(e);
		if (tmp != null) {
			tmp = Chunk.containingCunk(tmp);
			if (view.containsKey(tmp)) {
				view.get(tmp).removeEntity(e);
			}
			if (managedChunks.containsKey(tmp)) {
				managedChunks.get(tmp).removeEntity(e);
			}
		}
		tmp = Chunk.containingCunk(coord);
		if (view.containsKey(tmp)) {
			view.get(tmp).setEntityCoordinate(e, coord.getX(), coord.getY());
		}
		if (managedChunks.containsKey(tmp)) {
			managedChunks.get(tmp).setEntityCoordinate(
					e, coord.getX(), coord.getY());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void manageChunk(final Chunk chunk) {
		if (!managedChunks.containsKey(chunk.getPosition())) {
			managedChunks.put(chunk.getPosition(), chunk);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Chunk unmanageChunk(final TileCoordinate coord) {
		return managedChunks.remove(coord);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Chunk getChunk(final TileCoordinate coord) {
		Chunk c = managedChunks.get(coord);
		if (c == null) {
			c = chunkGenerator.generateChunk(coord);
			managedChunks.put(coord, c);
		}
		return c;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeEntity(final Entity e) {
		TileCoordinate c = findTile(e);
		if (c != null) {
			TileCoordinate chunkCoordinate = Chunk.containingCunk(c);
			if (view.containsKey(chunkCoordinate)) {
				view.get(chunkCoordinate).removeEntity(e);
			} else {
				managedChunks.get(chunkCoordinate).removeEntity(e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseChunk(final Chunk chunk) {
		int x, y;
		x = Math.abs(chunk.getX() - pos.getX());
		y = Math.abs(chunk.getY() - pos.getY());
		if (x <= 1 && y <= 1) {
			if(managedChunks.containsKey(chunk.getPosition())){
				view.put(chunk.getPosition(), managedChunks.get(chunk.getPosition()));
			}else{
				view.put(chunk.getPosition(), chunk);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(final Player p) {
		// TODO networking version!
		Chunk c;
		if (managedChunks.size() == 0) {
			c = chunkGenerator.generateChunk(new TileCoordinate(0, 0));
			managedChunks.put(c.getPosition(), c);
			view.put(c.getPosition(), c);
			pos = new TileCoordinate(0, 0);
		} else {
			c = managedChunks.get(managedChunks.keys().nextElement());
			view.put(c.getPosition(), c);
			pos = c.getPosition();
		}
		int cx,cy;
		cx = c.getX()*Chunk.CHUNK_SIZE;
		cy= c.getY()*Chunk.CHUNK_SIZE;
		Random r = new Random();
		boolean set = false;
		while (!set) {
			final int x = r.nextInt(Chunk.CHUNK_SIZE);
			final int y = r.nextInt(Chunk.CHUNK_SIZE);
			if (c.getTile(x, y).getEntityOnTile() == null
					&& c.getTile(x, y).getHeight() == Tile.GROUND_HEIGHT) {
				c.setEntityCoordinate(p, cx+x, cy+y);
				set = true;
			}
		}
		updateView();
		
	}

	/**
	 * method will request all chunks
	 */
	private void updateView() {
		for (int x = pos.getX() - 1; x <= pos.getX() + 1; x++) {
			for (int y = pos.getX() - 1; y <= pos.getY()+1; y++) {
				if (!view.containsKey(new TileCoordinate(x, y))) {
					if (!managedChunks.containsKey(new TileCoordinate(x, y))) {
						chunkManager.requestChunk(new TileCoordinate(x, y));
					} else {
						view.put(new TileCoordinate(x, y),
								managedChunks.get(new TileCoordinate(x, y)));
					}
				}
			}
		}
	}

	/**
	 * method will remove all chunks no longer needed to buffer
	 */
	private void cleanView() {
		int x, y;
		ArrayList<TileCoordinate> toRemove = new ArrayList<TileCoordinate>();
		for (TileCoordinate coord : view.keySet()) {
			x = Math.abs(coord.getX() - pos.getX());
			y = Math.abs(coord.getY() - pos.getY());
			if(x > 1 || y > 1){
				toRemove.add(coord);
			}
		}
		for (TileCoordinate tileCoordinate : toRemove) {
			view.remove(tileCoordinate);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setManager(IChunkManager chunkManager) {
		this.chunkManager = chunkManager;
	}

}
