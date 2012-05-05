package org.blockout.world;

import java.io.InputStream;
import java.net.URL;

import org.blockout.engine.SpriteType;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.util.ResourceLocation;

public class StaticTMXWorld implements IWorld {

	private TiledMap	map;

	public StaticTMXWorld(final String tmxFile) {
		try {

			ResourceLoader.addResourceLocation( new ResourceLocation() {

				@Override
				public InputStream getResourceAsStream( final String name ) {
					return getClass().getResourceAsStream( name );
				}

				@Override
				public URL getResource( final String name ) {
					return getClass().getResource( name );
				}
			} );
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
		int objectId = map.getTileId( x + 50, y + 50, 1 ) - 1;
		if ( objectId == 586 ) {
			return new Tile( spriteId, SpriteType.Crate );
		}
		if ( objectId == 249 ) {
			return new Tile( spriteId, SpriteType.Zombie );
		}
		if ( objectId == 250 ) {
			return new Tile( spriteId, SpriteType.Skeleton );
		}
		return new Tile( spriteId, (spriteId == 849 /* StoneGround Dark */) ? Tile.WALL_HEIGHT : Tile.GROUND_HEIGHT );
	}
}
