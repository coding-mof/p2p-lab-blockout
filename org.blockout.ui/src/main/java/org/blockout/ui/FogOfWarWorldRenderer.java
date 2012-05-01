package org.blockout.ui;

import org.blockout.engine.ISpriteManager;
import org.blockout.engine.SpriteMapping;
import org.blockout.world.IWorld;
import org.blockout.world.Tile;
import org.newdawn.slick.Image;

public class FogOfWarWorldRenderer extends AbstractWorldRenderer {

	private final ISpriteManager	spriteManager;
	private final SpriteMapping		mapping;

	public FogOfWarWorldRenderer(final ISpriteManager spriteManager, final IWorld world, final int tileSize,
			final int width, final int height) {
		super( world, tileSize, width, height );

		this.spriteManager = spriteManager;
		mapping = new SpriteMapping();
	}

	@Override
	protected void renderTile( final Tile tile, final int worldX, final int worldY, final int screenX, final int screenY ) {
		try {
			Image sprite = spriteManager.getSprite( mapping.getSpriteType( tile.getTileType() ) );
			if ( sprite != null ) {
				sprite.draw( screenX, screenY );
			}
		} catch ( IllegalArgumentException e ) {
		}
	}

}
