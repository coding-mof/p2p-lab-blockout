package org.blockout.ui;

import org.blockout.world.Tile;
import org.newdawn.slick.Graphics;

public interface IRenderingPass {

	public void beginPass();

	public void renderTile( Graphics g, Tile tile, int worldX, int worldY, int screenX, int screenY );

	public void renderMissingTile( Graphics g, int worldX, int worldY, int screenX, int screenY );

	public void endPass();
}
