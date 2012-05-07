package org.blockout.ui;

import org.blockout.world.IWorld;
import org.blockout.world.Tile;
import org.newdawn.slick.Graphics;

import com.google.common.base.Preconditions;

/**
 * Abstract base class for a world renderer which provides a basic algorithm for
 * computing which tiles to render and at which location of the screen.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public abstract class AbstractWorldRenderer implements IWorldRenderer {

	private final IWorld	world;

	protected Camera		camera;

	/**
	 * Constructs a new instance.
	 * 
	 * @param world
	 *            The world used for obtaining tiles.
	 * @param camera
	 *            The camera.
	 * @throws IllegalArgumentException
	 *             If tileSize, displayWidth or displayHeight or negative.
	 * @throws NullPointerException
	 *             If you pass in null as world or camera.
	 */
	public AbstractWorldRenderer(final IWorld world, final Camera camera) {
		Preconditions.checkNotNull( world );
		Preconditions.checkNotNull( camera );

		this.world = world;
		this.camera = camera;
	}

	@Override
	public void render( final Graphics g ) {

		camera.lock();
		try {

			int tileSize = camera.getTileSize();
			int startTileX = camera.getStartTileX();
			int startTileY = camera.getStartTileY();

			int curX;
			int curY = -camera.getHeightOfset();
			for ( int y = 0; y < camera.getNumVerTiles(); y++ ) {
				curX = -camera.getWidthOfset();
				for ( int x = 0; x < camera.getNumHorTiles(); x++ ) {

					Tile tile = world.getTile( startTileX + x, startTileY + y );
					if ( tile != null ) {
						renderTile( g, tile, startTileX + x, startTileY + y, curX, camera.convertY( curY ) - tileSize );
					} else {
						renderMissingTile( g, startTileX + x, startTileY + y, curX, camera.convertY( curY ) - tileSize );
					}

					curX += tileSize;
				}
				curY += tileSize;
			}

			curY = -camera.getHeightOfset();
			for ( int y = 0; y < camera.getNumVerTiles(); y++ ) {
				curX = -camera.getWidthOfset();
				for ( int x = 0; x < camera.getNumHorTiles(); x++ ) {

					Tile tile = world.getTile( startTileX + x, startTileY + y );
					if ( tile != null ) {
						renderTileOverlay( g, tile, startTileX + x, startTileY + y, curX, camera.convertY( curY )
								- tileSize );
					}

					curX += tileSize;
				}
				curY += tileSize;

			}
		} finally {
			camera.unlock();
		}
	}

	/**
	 * Invoked for each tile which should be rendered by sub-classes. This
	 * method doesn't get invoked for tiles which are currently not available.
	 * 
	 * @param g
	 *            The Graphics object.
	 * @param tile
	 *            The tile to render.
	 * @param worldX
	 *            The x coordinate of the tile relative to the world's origin.
	 * @param worldY
	 *            The y coordinate of the tile relative to the world's origin.
	 * @param screenX
	 *            The x coordinate where the tile has to be rendered in screen
	 *            coordinates.
	 * @param screenY
	 *            The y coordinate where the tile has to be rendered in screen
	 *            coordinates.
	 */
	protected abstract void renderTile( Graphics g, Tile tile, int worldX, int worldY, int screenX, int screenY );

	protected abstract void renderTileOverlay( Graphics g, Tile tile, int worldX, int worldY, int screenX, int screenY );

	/**
	 * Invoked for each tile which is currently not available. Overwrite this
	 * method to implement some custom rendering. The default implementation of
	 * the method simply does nothing which leaves the tile black.
	 * 
	 * @param worldX
	 *            The x coordinate of the tile relative to the world's origin.
	 * @param worldY
	 *            The y coordinate of the tile relative to the world's origin.
	 * @param screenX
	 *            The x coordinate where the tile has to be rendered in screen
	 *            coordinates.
	 * @param screenY
	 *            The y coordinate where the tile has to be rendered in screen
	 *            coordinates.
	 */
	protected void renderMissingTile( final Graphics g, final int worldX, final int worldY, final int screenX,
			final int screenY ) {
		// default implementation is to render nothing and leave the space black
	}
}
