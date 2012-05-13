package org.blockout.ui;

import org.blockout.world.Tile;
import org.newdawn.slick.Graphics;

/**
 * Common interface for all rendering passes of the
 * {@link AbstractWorldRenderer}.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public interface IRenderingPass {

	/**
	 * Gets invoked directly before the rendering of this pass starts.
	 */
	public void beginPass();

	/**
	 * Gets invoked for each tile that should get rendered.
	 * 
	 * @param g
	 *            The graphics instance to render primitives.
	 * @param tile
	 *            The tile to render.
	 * @param worldX
	 *            The x coordinate of the tile relative to the world origin.
	 * @param worldY
	 *            The y coordinate of the tile relative to the world origin.
	 * @param screenX
	 *            The x coordinate of the tile in screen coordinates.
	 * @param screenY
	 *            The y coordinate of the tile in screen coordinates.
	 */
	public void renderTile( Graphics g, Tile tile, int worldX, int worldY, int screenX, int screenY );

	/**
	 * Gets invoked for each tile that is currently not available.
	 * 
	 * @param g
	 *            The graphics instance to render primitives.
	 * @param worldX
	 *            The x coordinate of the tile relative to the world origin.
	 * @param worldY
	 *            The y coordinate of the tile relative to the world origin.
	 * @param screenX
	 *            The x coordinate of the tile in screen coordinates.
	 * @param screenY
	 *            The y coordinate of the tile in screen coordinates.
	 */
	public void renderMissingTile( Graphics g, int worldX, int worldY, int screenX, int screenY );

	/**
	 * Gets invoked after rendering of this pass has been completed.
	 */
	public void endPass();
}
