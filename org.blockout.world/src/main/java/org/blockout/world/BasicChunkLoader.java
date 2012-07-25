package org.blockout.world;

import java.io.File;
import java.io.FileFilter;

import org.blockout.common.TileCoordinate;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import com.google.common.base.Preconditions;

public class BasicChunkLoader implements IChunkLoader {
	
	
	private String mapfolder;
	
	public BasicChunkLoader(String mapfolder) {
		Preconditions.checkNotNull(mapfolder);
		this.mapfolder = mapfolder;
	}
	

	@Override
	public Chunk loadChunk( final TileCoordinate coordinate, final String tmxFile ) {
		return new Chunk( coordinate, loadTiles( tmxFile ) );
	}

	private Tile[][] loadTiles( final String tmxFile ) {
		TiledMap map = null;
		try {
			map = new TiledMap( tmxFile, false );
		} catch ( SlickException e ) {
			throw new RuntimeException( "", e );
		}
		Preconditions.checkArgument( map.getHeight() == Chunk.CHUNK_SIZE,
				"Chunks should have a height of %s but got %s", Chunk.CHUNK_SIZE, map.getHeight() );
		Preconditions.checkArgument( map.getWidth() == Chunk.CHUNK_SIZE, "Chunks should have a width of %s but got %s",
				Chunk.CHUNK_SIZE, map.getWidth() );

		Tile[][] tiles = new Tile[map.getWidth()][map.getHeight()];

		for ( int x = 0; x < Chunk.CHUNK_SIZE; x++ ) {
			for ( int y = 0; y < Chunk.CHUNK_SIZE; y++ ) {
				int spriteID = map.getTileId( x, y, 0 );
				int objectID = 0;
				if(map.getLayerCount()>1){
					objectID = map.getTileId( x, y, 1 );
				}
				if ( objectID >= 0 ) {
					tiles[x][y] = new Tile( objectID, Tile.WALL_HEIGHT );
				} else if ( spriteID >= 0 ) {
					tiles[x][y] = new Tile( spriteID, Tile.GROUND_HEIGHT );
				}
			}
		}
		return tiles;
	}


	@Override
	public File[] getMaps() {
		  // Directory path here
		  File folder = new File(mapfolder);
		  File[] listOfFiles = folder.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().endsWith(".tmx"))
		          {
		               return true;
		          }
		          return true;
			}
		});
		  
		  return folder.listFiles();
	}
}
