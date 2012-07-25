package org.blockout.world;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Crate;
import org.blockout.world.entity.Entity;
import org.blockout.world.entity.Monster;
import org.blockout.world.entity.Player;
import org.blockout.world.entity.Zombie;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.util.ResourceLocation;

/**
 * Simple implementation of a static world using a TMX-File.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class StaticTMXWorld implements IWorld {

	private TiledMap						map;
	private HashMap<TileCoordinate, Tile>	tileCache;
	private HashMap<Entity, TileCoordinate>	entityCache;

	public StaticTMXWorld(final String tmxFile) {
		tileCache = new HashMap<TileCoordinate, Tile>();
		entityCache = new HashMap<Entity, TileCoordinate>();
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
		return getTile( new TileCoordinate( x, y ) );
	}

	protected void putEntity( final Entity e, final int x, final int y ) {
		entityCache.put( e, new TileCoordinate( x, y ) );
	}

	@Override
	public TileCoordinate findTile( final Entity entity ) {
		return entityCache.get( entity );
	}

	@Override
	public void setPlayerPosition( final Player p, final TileCoordinate coord ) {
		entityCache.put( p, coord );
	}

	@Override
	public void setEnityPosition( final Entity e, final TileCoordinate coord ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeEntity( final Entity e ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init( final Player p ) {
		// TODO Auto-generated method stub

	}

	@Override
	public Tile getTile( final TileCoordinate coord ) {
		if ( coord.getX() <= -50 || coord.getY() <= -50 ) {
			return null;
		}
		Tile t = tileCache.get( coord );
		if ( t == null ) {
			int spriteId = map.getTileId( coord.getX() + 50, coord.getY() + 50, 0 ) - 1;
			if ( spriteId < 0 ) {
				return null;
			}
			int objectId = map.getTileId( coord.getX() + 50, coord.getY() + 50, 1 ) - 1;
			if ( objectId == 586 ) {
				Crate entity = new Crate();
				t = new Tile( spriteId, entity );
				putEntity( entity, coord.getX(), coord.getY() );
			} else if ( objectId == 249 ) {
				Zombie entity = new Zombie( 1 );
				t = new Tile( spriteId, entity );
				putEntity( entity, coord.getX(), coord.getY() );
			} else if ( objectId == 250 ) {
				Monster entity = new Monster( "Skeleton", 1, 100, 25 );
				t = new Tile( spriteId, entity );
				putEntity( entity, coord.getX(), coord.getY() );
			} else {
				t = new Tile( spriteId, getSpriteTypeHeight( spriteId ) );
			}
		}
		tileCache.put( coord, t );
		return t;
	}

	private int getSpriteTypeHeight( final int spriteId ) {
		switch ( spriteId ) {
			case 841: // stoneground_mid
			case 842: // open door
			case 843: // open_door
			case 848: // stoneground
				// case 849: // stoneground_dark
				// should be walls...^^
			case 850: // stoneground_mid
			case 861: // ice
			case 863: // lowered_drawbridge_vertical
			case 864: // lowered_drawbridge_horizontal
				return Tile.GROUND_HEIGHT;

			default:
				return Tile.WALL_HEIGHT;
		}
	}

	@Override
	public <T extends Entity> T findEntity( final UUID id, final Class<T> clazz ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return true;
	}
}
