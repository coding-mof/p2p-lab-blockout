package org.blockout.world;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class StaticTMXWorld implements IWorld {

	private TiledMap	map;

	public StaticTMXWorld(final String tmxFile) {
		try {
			map = new TiledMap( tmxFile );

		} catch ( SlickException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Tile getTile( final int x, final int y ) {
		return new Tile( map.getTileId( x, y, 0 ) );

	}

}
