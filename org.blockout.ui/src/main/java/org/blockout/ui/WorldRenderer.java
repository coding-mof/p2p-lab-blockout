package org.blockout.ui;

import org.blockout.engine.ISpriteManager;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class WorldRenderer {

	private ISpriteManager	spriteManager;

	private float			centerX;
	private float			centerY;

	private final int		width;
	private final int		height;

	private final float		halfWidth;
	private final float		halfHeight;

	private int				numHorTiles;
	private int				numVerTiles;

	private final int		tileSize;

	private int				widthOfset;
	private int				heightOfset;

	public WorldRenderer(final ISpriteManager spriteManager, final int tileSize, final int width, final int height) {
		this.tileSize = tileSize;

		this.width = width;
		this.height = height;

		halfWidth = width / 2f;
		halfHeight = height / 2f;

		numHorTiles = (int) Math.ceil( width / ((double) tileSize) );
		if ( width % tileSize == 0 ) {
			numHorTiles++;
		}
		numVerTiles = (int) Math.ceil( height / ((double) tileSize) );
		if ( height % tileSize == 0 ) {
			numVerTiles++;
		}
	}

	public void setViewCenter( final float x, final float y ) {
		centerX = x;
		centerY = y;
	}

	private int convertY( final int y ) {
		return height - y;
	}

	public void render( final Graphics g, final Image sprite ) {

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

				// Tile tile = World.getTile(startTileX + x, startTileY + y);
				// Image sprite = spriteManager.getSprite( /* tile.getType()
				// */SpriteType.StoneGround );
				sprite.draw( curX, convertY( curY ) - tileSize );

				curX += tileSize;
			}
			curY += tileSize;
		}
	}
}
