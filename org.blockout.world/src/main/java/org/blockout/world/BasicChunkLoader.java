package org.blockout.world;

import javax.inject.Named;

import org.blockout.common.TileCoordinate;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import com.google.common.base.Preconditions;

@Named
public class BasicChunkLoader implements IChunkLoader {

	@Override
	public Chunk loadChunk(TileCoordinate coordinate, String tmxFile) {
		return new Chunk(coordinate, loadTiles(tmxFile));
	}

	private Tile[][] loadTiles(String tmxFile) {
		TiledMap map = null;
		try {
			map = new TiledMap(tmxFile, false);
		} catch (SlickException e) {
			throw new RuntimeException("", e);
		}
		Preconditions.checkArgument(map.getHeight() == Chunk.CHUNK_SIZE,
				"Chunks should have a height of %s but got %s",
				Chunk.CHUNK_SIZE, map.getHeight());
		Preconditions.checkArgument(map.getWidth() == Chunk.CHUNK_SIZE,
				"Chunks should have a width of %s but got %s",
				Chunk.CHUNK_SIZE, map.getWidth());

		Tile[][] tiles = new Tile[map.getWidth()][map.getHeight()];

		for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
			for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
				int spriteID = map.getTileId(x, y, 0);
				int objectID = map.getTileId(x, y, 1);
				if (objectID >= 0) {
					tiles[x][y] = new Tile(objectID, Tile.WALL_HEIGHT);
				} else if (spriteID >= 0) {
					tiles[x][y] = new Tile(spriteID, Tile.GROUND_HEIGHT);
				}
			}
		}
		return null;
	}
}
