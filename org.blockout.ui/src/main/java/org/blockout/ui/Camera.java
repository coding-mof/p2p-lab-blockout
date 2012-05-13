package org.blockout.ui;

import java.util.concurrent.locks.ReentrantLock;

import com.google.common.base.Preconditions;

/**
 * This class is responsible for computing the view frustum, visible tiles,
 * rendering ofsets, etc. Since this class is heavily used during rendering
 * synchronization might lead to performance decrease. Therefore this class is
 * not itself synchronized but allows the using code to lock the camera settings
 * using a lightweight locking mechanism using {@link Camera#lock()} and
 * {@link Camera#unlock()}.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class Camera {

	/**
	 * The view center in world coordinates.
	 */
	protected float				centerX;
	/**
	 * The view center in world coordinates.
	 */
	protected float				centerY;
	/**
	 * The width of the viewport in pixel.
	 */
	protected int				width;
	/**
	 * The height of the viewport in pixel.
	 */
	protected int				height;
	/**
	 * Half of the width of the viewport in pixel.
	 */
	protected float				halfWidth;
	/**
	 * Half of the height of the viewport in pixel.
	 */
	protected float				halfHeight;
	/**
	 * Number of horizontal tiles that are visible in the viewport.
	 */
	protected int				numHorTiles;
	/**
	 * Number of vertical tiles that are visible in the viewport.
	 */
	protected int				numVerTiles;
	/**
	 * Size of a tile in pixel. Tiles are assumed to be squares.
	 */
	protected int				tileSize;
	/**
	 * The ofset between the viewport and the first tile to render (in pixel).
	 */
	protected int				widthOfset;
	/**
	 * The ofset between the viewport and the first tile to render (in pixel).
	 */
	protected int				heightOfset;
	/**
	 * The id of the first horizontal tile to render.
	 */
	protected int				startTileX;
	/**
	 * The id of the first vertical tile to render.
	 */
	protected int				startTileY;
	/**
	 * Lock for synchronizing access to the camera.
	 */
	private final ReentrantLock	lock	= new ReentrantLock();

	public Camera(final int tileSize, final int displayWidth, final int displayHeight) {
		Preconditions.checkArgument( tileSize > 0, "Tile size must be positive." );
		Preconditions.checkArgument( displayHeight > 0, "Display height must be positive." );
		Preconditions.checkArgument( displayWidth > 0, "Display width must be positive." );

		this.tileSize = tileSize;

		setBounds( displayWidth, displayHeight );
	}

	public void setBounds( final int displayWidth, final int displayHeight ) {
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
		adjustOfsets();
	}

	/**
	 * Locks the camera. Synchronization does only work if all threads are
	 * locking the camera before they are going to read or modify it's state.
	 * 
	 * @see ReentrantLock
	 */
	public void lock() {
		lock.lock();
	}

	/**
	 * Unlocks the camera to let other threads access it's state.
	 * 
	 * @see ReentrantLock
	 */
	public void unlock() {
		lock.unlock();
	}

	public float getCenterX() {
		return centerX;
	}

	public float getCenterY() {
		return centerY;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public float getHalfWidth() {
		return halfWidth;
	}

	public float getHalfHeight() {
		return halfHeight;
	}

	public int getNumHorTiles() {
		return numHorTiles;
	}

	public int getNumVerTiles() {
		return numVerTiles;
	}

	public int getTileSize() {
		return tileSize;
	}

	public int getWidthOfset() {
		return widthOfset;
	}

	public int getHeightOfset() {
		return heightOfset;
	}

	public int getStartTileX() {
		return startTileX;
	}

	public int getStartTileY() {
		return startTileY;
	}

	public void setViewCenter( final float x, final float y ) {
		centerX = x;
		centerY = y;
		adjustOfsets();
	}

	private void adjustOfsets() {
		startTileX = worldToTile( centerX - (halfWidth / tileSize) );
		startTileY = worldToTile( centerY - (halfHeight / tileSize) );

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
	}

	public int convertY( final int y ) {
		return height - y;
	}

	public int worldToTile( final float worldCoordinate ) {
		if ( worldCoordinate >= 0 ) {
			return (int) worldCoordinate;
		}
		return (int) worldCoordinate - 1;
	}

	public float screenToWorldX( final int screenX ) {
		float worldX0 = centerX - (halfWidth / tileSize);
		return worldX0 + (screenX / ((float) tileSize));
	}

	public float screenToWorldY( final int screenY ) {
		float worldY0 = centerY - (halfHeight / tileSize);
		return worldY0 + (convertY( screenY ) / ((float) tileSize));
	}
}
