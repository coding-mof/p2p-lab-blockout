package org.blockout.ui;

import java.util.concurrent.locks.ReentrantLock;

import com.google.common.base.Preconditions;

public class Camera {

	protected float				centerX;
	protected float				centerY;

	protected int				width;
	protected int				height;

	protected float				halfWidth;
	protected float				halfHeight;

	protected int				numHorTiles;
	protected int				numVerTiles;

	protected int				tileSize;

	protected int				widthOfset;
	protected int				heightOfset;
	private int					startTileX;
	private int					startTileY;

	private final ReentrantLock	lock	= new ReentrantLock();

	public Camera(final int tileSize, final int displayWidth, final int displayHeight) {
		Preconditions.checkArgument( tileSize > 0, "Tile size must be positive." );
		Preconditions.checkArgument( displayHeight > 0, "Display height must be positive." );
		Preconditions.checkArgument( displayWidth > 0, "Display width must be positive." );

		this.tileSize = tileSize;

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

	public void lock() {
		lock.lock();
	}

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
		if ( worldCoordinate > 0 ) {
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
