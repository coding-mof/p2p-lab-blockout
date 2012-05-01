package org.blockout.world;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class StaticTMXWorld implements IWorld {

	private TiledMap	map;

	public StaticTMXWorld(final String tmxFile) {
		try {
			map = new TiledMap( tmxFile, false );

		} catch ( SlickException e ) {
			throw new RuntimeException( "", e );
		}
	}

	@Override
	public Tile getTile( final int x, final int y ) {
		if ( x <= 0 || y <= 0 ) {
			return null;
		}
		return new Tile( map.getTileId( x, y, 0 ) );
	}

}
