package org.blockout.ui;

import org.blockout.engine.ISpriteManager;
import org.blockout.engine.SpriteMapping;
import org.blockout.engine.SpriteType;
import org.blockout.logic.FogOfWar;
import org.blockout.world.IWorld;
import org.blockout.world.Tile;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class FogOfWarWorldRenderer extends AbstractWorldRenderer {

	private static final float		VIEW_DISTANCE			= 5f;
	private static final float		SQUARED_VIEW_DISTANCE	= VIEW_DISTANCE * VIEW_DISTANCE;

	private final ISpriteManager	spriteManager;
	private final SpriteMapping		mapping;
	private final FogOfWar			fog;

	public FogOfWarWorldRenderer(final ISpriteManager spriteManager, final IWorld world, final int tileSize,
			final int width, final int height) {
		super( world, tileSize, width, height );

		this.spriteManager = spriteManager;
		mapping = new SpriteMapping();
		fog = new FogOfWar();
	}

	public void render( final Graphics g, final Image player ) {
		super.render( g );

		player.drawCentered( width / 2, height / 2 );
	}

	@Override
	protected void renderTile( final Tile tile, final int worldX, final int worldY, final int screenX, final int screenY ) {

		// only render explored tiles
		if ( !fog.isExplored( worldX, worldY ) ) {
			return;
		}

		try {
			SpriteType spriteType = mapping.getSpriteType( tile.getTileType() );
			Image sprite = spriteManager.getSprite( spriteType );
			if ( sprite != null ) {

				float alpha = computeLightning( worldX, worldY, sprite );
				sprite.draw( screenX, screenY );

				if ( alpha > 0.5f ) {
					Object o = tile.getObjectOnTile();
					if ( o != null ) {
						spriteType = (SpriteType) o;
						sprite = spriteManager.getSprite( spriteType );
						computeLightning( worldX, worldY, sprite );
						sprite.draw( screenX, screenY );
					}
				}
			}
		} catch ( IllegalArgumentException e ) {
			// unknown sprite
		}
	}

	private float computeLightning( final int worldX, final int worldY, final Image sprite ) {
		double origDistX = Math.pow( worldX - centerX, 2 );
		double distXP1 = Math.pow( worldX + 1 - centerX, 2 );
		double origDistY = Math.pow( worldY - centerY, 2 );
		double distYP1 = Math.pow( worldY + 1 - centerY, 2 );

		float overallAlpha = 0;
		overallAlpha += computeLightningBottomLeft( sprite, origDistX, origDistY );
		overallAlpha += computeLightningBottomRight( distXP1, worldX, sprite, origDistY );
		overallAlpha += computeLightningTopRight( distYP1, worldY, sprite, distXP1 );
		overallAlpha += computeLightningTopLeft( sprite, origDistX, distYP1 );
		return overallAlpha / 4f;
	}

	private float computeLightningBottomLeft( final Image sprite, final double origDistX, final double origDistY ) {
		if ( origDistX + origDistY < SQUARED_VIEW_DISTANCE ) {
			sprite.setColor( Image.BOTTOM_LEFT, 1, 1, 1, 1 );
			return 1;
		} else {
			sprite.setColor( Image.BOTTOM_LEFT, 1, 1, 1, 0.5f );
			return 0.5f;
		}
	}

	private float computeLightningBottomRight( final double distXP1, final int worldX, final Image sprite,
			final double origDistY ) {

		if ( distXP1 + origDistY < SQUARED_VIEW_DISTANCE ) {
			sprite.setColor( Image.BOTTOM_RIGHT, 1, 1, 1, 1 );
			return 1;
		} else {
			sprite.setColor( Image.BOTTOM_RIGHT, 1, 1, 1, 0.5f );
			return 0.5f;
		}
	}

	private float computeLightningTopRight( final double distYP1, final int worldY, final Image sprite,
			final double distXP1 ) {

		if ( distXP1 + distYP1 < SQUARED_VIEW_DISTANCE ) {
			sprite.setColor( Image.TOP_RIGHT, 1, 1, 1, 1 );
			return 1;
		} else {
			sprite.setColor( Image.TOP_RIGHT, 1, 1, 1, 0.5f );
			return 0.5f;
		}
	}

	private float computeLightningTopLeft( final Image sprite, final double origDistX, final double distYP1 ) {
		if ( origDistX + distYP1 < SQUARED_VIEW_DISTANCE ) {
			sprite.setColor( Image.TOP_LEFT, 1, 1, 1, 1 );
			return 1;
		} else {
			sprite.setColor( Image.TOP_LEFT, 1, 1, 1, 0.5f );
			return 0.5f;
		}
	}

	@Override
	public void setViewCenter( final float x, final float y ) {
		super.setViewCenter( x, y );

		// TODO: move this code to the MovementHandler
		fog.setExplored( (int) x - 2, (int) y - 2, true );
		fog.setExplored( (int) x - 1, (int) y - 2, true );
		fog.setExplored( (int) x, (int) y - 2, true );
		fog.setExplored( (int) x + 1, (int) y - 2, true );
		fog.setExplored( (int) x + 2, (int) y - 2, true );

		fog.setExplored( (int) x - 2, (int) y - 1, true );
		fog.setExplored( (int) x - 1, (int) y - 1, true );
		fog.setExplored( (int) x, (int) y - 1, true );
		fog.setExplored( (int) x + 1, (int) y - 1, true );
		fog.setExplored( (int) x + 2, (int) y - 1, true );

		fog.setExplored( (int) x - 2, (int) y, true );
		fog.setExplored( (int) x - 1, (int) y, true );
		fog.setExplored( (int) x, (int) y, true );
		fog.setExplored( (int) x + 1, (int) y, true );
		fog.setExplored( (int) x + 2, (int) y, true );

		fog.setExplored( (int) x - 2, (int) y + 1, true );
		fog.setExplored( (int) x - 1, (int) y + 1, true );
		fog.setExplored( (int) x, (int) y + 1, true );
		fog.setExplored( (int) x + 1, (int) y + 1, true );
		fog.setExplored( (int) x + 2, (int) y + 1, true );

		fog.setExplored( (int) x - 2, (int) y + 2, true );
		fog.setExplored( (int) x - 1, (int) y + 2, true );
		fog.setExplored( (int) x, (int) y + 2, true );
		fog.setExplored( (int) x + 1, (int) y + 2, true );
		fog.setExplored( (int) x + 2, (int) y + 2, true );
	}

}
