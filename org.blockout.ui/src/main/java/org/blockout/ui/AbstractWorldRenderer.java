package org.blockout.ui;

import java.util.Vector;

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

	// Dependencies
	protected final IWorld				world;
	protected Camera					camera;

	// Own state
	protected Vector<IRenderingPass>	renderingPass;

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

		renderingPass = new Vector<IRenderingPass>();

		this.world = world;
		this.camera = camera;
	}

	public void addRenderingPass( final IRenderingPass pass ) {
		renderingPass.add( pass );
	}

	@Override
	public void render( final Graphics g ) {

		camera.lock();
		try {

			int tileSize = camera.getTileSize();
			int startTileX = camera.getStartTileX();
			int startTileY = camera.getStartTileY();

			int curX;
			int curY;
			for ( IRenderingPass pass : renderingPass ) {
				pass.beginPass();
				curY = -camera.getHeightOfset();
				for ( int y = 0; y < camera.getNumVerTiles(); y++ ) {
					curX = -camera.getWidthOfset();
					for ( int x = 0; x < camera.getNumHorTiles(); x++ ) {

						Tile tile = world.getTile( startTileX + x, startTileY + y );
						if ( tile != null ) {
							pass.renderTile( g, tile, startTileX + x, startTileY + y, curX, camera.convertY( curY )
									- tileSize );
						} else {
							pass.renderMissingTile( g, startTileX + x, startTileY + y, curX, camera.convertY( curY )
									- tileSize );
						}

						curX += tileSize;
					}
					curY += tileSize;
				}
				pass.endPass();
			}

		} finally {
			camera.unlock();
		}
	}
}
