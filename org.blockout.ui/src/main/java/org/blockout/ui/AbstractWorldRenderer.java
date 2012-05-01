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

	protected float			centerX;
	protected float			centerY;

	protected final int		width;
	protected final int		height;

	private final float		halfWidth;
	private final float		halfHeight;

	protected int			numHorTiles;
	protected int			numVerTiles;

	private final int		tileSize;

	private int				widthOfset;
	private int				heightOfset;

	/**
	 * Constructs a new instance.
	 * 
	 * @param world
	 *            The world used for obtaining tiles.
	 * @param tileSize
	 *            The size of a tile measured in pixels.
	 * @param displayWidth
	 *            The width of the display measured in pixels.
	 * @param displayHeight
	 *            The height of the display measured in pixels.
	 * @throws IllegalArgumentException
	 *             If tileSize, displayWidth or displayHeight or negative.
	 * @throws NullPointerException
	 *             If you pass in null as world.
	 */
	public AbstractWorldRenderer(final IWorld world, final int tileSize, final int displayWidth, final int displayHeight) {
		Preconditions.checkNotNull( world );
		Preconditions.checkArgument( tileSize > 0, "Tile size must be positive." );
		Preconditions.checkArgument( displayHeight > 0, "Display height must be positive." );
		Preconditions.checkArgument( displayWidth > 0, "Display width must be positive." );

		this.tileSize = tileSize;
		this.world = world;

		width = displayWidth;
		height = displayHeight;

		halfWidth = displayWidth / 2f;
		halfHeight = displayHeight / 2f;

		numHorTiles = (int) Math.ceil( displayWidth / ((double) tileSize) );
		if ( displayWidth % tileSize == 0 ) {
			numHorTiles++;
		}
		numVerTiles = (int) Math.ceil( displayHeight / ((double) tileSize) );
		if ( displayHeight % tileSize == 0 ) {
			numVerTiles++;
		}
	}

	@Override
	public void setViewCenter( final float x, final float y ) {
		centerX = x;
		centerY = y;
	}

	private int convertY( final int y ) {
		return height - y;
	}

	@Override
	public void render( final Graphics g ) {

		int startTileX = (int) (centerX - (halfWidth / tileSize));
		int startTileY = (int) (centerY - (halfHeight / tileSize));

		float tmpWidth = (centerX - (halfWidth / tileSize)) % 1;
		if ( tmpWidth < 0 ) {
			tmpWidth++;
		}
		widthOfset = (int) (tmpWidth * tileSize);
		float tmpHeight = (centerY - (halfHeight / tileSize)) % 1;
		if ( tmpHeight < 0 ) {
			tmpHeight++;
		}
		heightOfset = (int) (tmpHeight * tileSize);

		int curX;
		int curY = -heightOfset;
		for ( int y = 0; y < numVerTiles; y++ ) {
			curX = -widthOfset;
			for ( int x = 0; x < numHorTiles; x++ ) {

				Tile tile = world.getTile( startTileX + x, startTileY + y );
				if ( tile != null ) {
					renderTile( tile, startTileX + x, startTileY + y, curX, convertY( curY ) - tileSize );
				} else {
					renderMissingTile( startTileX + x, startTileY + y, curX, convertY( curY ) - tileSize );
				}

				curX += tileSize;
			}
			curY += tileSize;
		}
	}

	/**
	 * Invoked for each tile which should be rendered by sub-classes. This
	 * method doesn't get invoked for tiles which are currently not available.
	 * 
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
	protected abstract void renderTile( Tile tile, int worldX, int worldY, int screenX, int screenY );

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
	protected void renderMissingTile( final int worldX, final int worldY, final int screenX, final int screenY ) {
		// default implementation is to render nothing and leave the space black
	}
}
