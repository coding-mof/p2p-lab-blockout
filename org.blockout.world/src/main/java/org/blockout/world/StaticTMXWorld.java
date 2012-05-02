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
		if ( x <= -50 || y <= -50 ) {
			return null;
		}
		int spriteId = map.getTileId( x + 50, y + 50, 0 ) - 1;
		return new Tile( spriteId, (spriteId == 848 /* StoneGround */) ? Tile.GROUND_HEIGHT : Tile.WALL_HEIGHT );
	}

}
