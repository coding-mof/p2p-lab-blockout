package org.blockout.world;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.blockout.common.TileCoordinate;

import com.google.common.base.Preconditions;

/**
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class DefaultChunkGenerator implements IChunkGenerator {

	private final File					mapDir;
	private final List<StaticTMXWorld>	tmxFiles;

	public DefaultChunkGenerator(final File mapDir) {

		Preconditions.checkNotNull( mapDir );
		if ( !mapDir.exists() || !mapDir.isDirectory() ) {
			throw new RuntimeException( "Can't find map directory " + mapDir );
		}

		this.mapDir = mapDir;
		tmxFiles = Collections.synchronizedList( new ArrayList<StaticTMXWorld>() );

		loadMaps();
	}

	private void loadMaps() {
		List<File> mapFiles = listMaps();
		for ( File mapFile : mapFiles ) {
			if ( !mapFile.isFile() ) {
				continue;
			}
			loadMap( mapFile );
		}
	}

	private List<File> listMaps() {
		List<File> mapFiles = Arrays.asList( mapDir.listFiles( new FileFilter() {

			@Override
			public boolean accept( final File pathname ) {
				return pathname.getName().endsWith( ".tmx" );
			}
		} ) );
		return mapFiles;
	}

	private void loadMap( final File mapFile ) {
		tmxFiles.add( new StaticTMXWorld( mapFile.getAbsolutePath() ) );
	}

	@Override
	public Chunk generateChunk( final TileCoordinate coordinate ) {
		Random rand = new Random( System.currentTimeMillis() );

		StaticTMXWorld tmxFile = tmxFiles.get( rand.nextInt( tmxFiles.size() ) );

		// copy tiles to new chunk and return
		// TODO Auto-generated method stub
		return null;
	}
}
