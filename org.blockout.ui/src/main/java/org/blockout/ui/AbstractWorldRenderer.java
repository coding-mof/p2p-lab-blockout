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

		Camera localCamera = camera.getReadOnly();

		int tileSize = localCamera.getTileSize();
		int startTileX = localCamera.getStartTileX();
		int startTileY = localCamera.getStartTileY();

		int curX;
		int curY;
		for ( IRenderingPass pass : renderingPass ) {
			pass.beginPass();
			curY = -localCamera.getHeightOfset();
			for ( int y = 0; y < localCamera.getNumVerTiles(); y++ ) {
				curX = -localCamera.getWidthOfset();
				for ( int x = 0; x < localCamera.getNumHorTiles(); x++ ) {

					Tile tile = world.getTile( startTileX + x, startTileY + y );
					if ( tile != null ) {
						pass.renderTile( g, tile, startTileX + x, startTileY + y, curX, localCamera.convertY( curY )
								- tileSize );
					} else {
						pass.renderMissingTile( g, startTileX + x, startTileY + y, curX, localCamera.convertY( curY )
								- tileSize );
					}

					curX += tileSize;
				}
				curY += tileSize;
			}
			pass.endPass();
		}

	}
}
