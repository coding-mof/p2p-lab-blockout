package org.blockout.world;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import org.blockout.world.entity.Monster;
import org.blockout.world.entity.Zombie;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.util.ResourceLocation;

public class StaticTMXWorld implements IWorld {

	private TiledMap				map;
	private HashMap<String, Tile>	tileCache;

	public StaticTMXWorld(final String tmxFile) {
		tileCache = new HashMap<String, Tile>();
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
		String key = "(" + x + "," + y + ")";
		Tile t = tileCache.get( key );
		if ( t == null ) {
			int spriteId = map.getTileId( x + 50, y + 50, 0 ) - 1;
			int objectId = map.getTileId( x + 50, y + 50, 1 ) - 1;
			if ( objectId == 586 ) {
				t = new Tile( spriteId, new Zombie( 1 ) ); // SpriteType.Crate
			} else if ( objectId == 249 ) {
				t = new Tile( spriteId, new Zombie( 1 ) ); // SpriteType.Zombie
			} else if ( objectId == 250 ) {
				t = new Tile( spriteId, new Monster( "Skeleton", 1, 100, 25 ) ); // SpriteType.Skeleton
			} else {
				t = new Tile( spriteId, (spriteId == 849 /* StoneGround Dark */) ? Tile.WALL_HEIGHT : Tile.GROUND_HEIGHT );
			}
		}
		tileCache.put( key, t );
		return t;
	}
}
