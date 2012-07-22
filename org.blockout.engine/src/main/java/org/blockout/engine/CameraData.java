package org.blockout.engine;

public class CameraData {
	/**
	 * The view center in world coordinates.
	 */
	public float	centerX;
	/**
	 * The view center in world coordinates.
	 */
	public float	centerY;
	/**
	 * The width of the viewport in pixel.
	 */
	public int		width;
	/**
	 * The height of the viewport in pixel.
	 */
	public int		height;
	/**
	 * Half of the width of the viewport in pixel.
	 */
	public float	halfWidth;
	/**
	 * Half of the height of the viewport in pixel.
	 */
	public float	halfHeight;
	/**
	 * Number of horizontal tiles that are visible in the viewport.
	 */
	public int		numHorTiles;
	/**
	 * Number of vertical tiles that are visible in the viewport.
	 */
	public int		numVerTiles;
	/**
	 * Size of a tile in pixel. Tiles are assumed to be squares.
	 */
	public int		tileSize;
	/**
	 * The ofset between the viewport and the first tile to render (in pixel).
	 */
	public int		widthOfset;
	/**
	 * The ofset between the viewport and the first tile to render (in pixel).
	 */
	public int		heightOfset;
	/**
	 * The id of the first horizontal tile to render.
	 */
	public int		startTileX;
	/**
	 * The id of the first vertical tile to render.
	 */
	public int		startTileY;

	public CameraData(final CameraData data) {
		centerX = data.centerX;
		centerY = data.centerY;
		width = data.width;
		height = data.height;
		halfWidth = data.halfWidth;
		halfHeight = data.halfHeight;
		numHorTiles = data.numHorTiles;
		numVerTiles = data.numVerTiles;
		tileSize = data.tileSize;
		widthOfset = data.widthOfset;
		heightOfset = data.heightOfset;
		startTileX = data.startTileX;
		startTileY = data.startTileY;
	}

	CameraData() {
	}
}