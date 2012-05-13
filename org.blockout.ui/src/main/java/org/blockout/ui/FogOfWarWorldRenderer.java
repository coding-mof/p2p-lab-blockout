package org.blockout.ui;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.engine.ISpriteManager;
import org.blockout.engine.Shader;
import org.blockout.engine.SpriteMapping;
import org.blockout.engine.SpriteType;
import org.blockout.logic.FogOfWar;
import org.blockout.world.IWorld;
import org.blockout.world.LocalGameState;
import org.blockout.world.Tile;
import org.blockout.world.entity.Entity;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

@Named
public class FogOfWarWorldRenderer extends AbstractWorldRenderer {

	private final ISpriteManager	spriteManager;
	private final SpriteMapping		mapping;
	private final FogOfWar			fog;
	protected LocalGameState		gameState;

	private Shader					shader;

	@Inject
	public FogOfWarWorldRenderer(final ISpriteManager spriteManager, final IWorld world, final Camera camera,
			final FogOfWar fog, final LocalGameState gameState) {
		super( world, camera );

		this.spriteManager = spriteManager;
		mapping = new SpriteMapping();
		this.fog = fog;

		this.gameState = gameState;

		addRenderingPass( new LightningComputationPass() );
		addRenderingPass( new EntityRenderPass() );
	}

	@Override
	public void render( final Graphics g ) {

		super.render( g );

		Image player = spriteManager.getSprite( SpriteType.Player, true );
		player.drawCentered( camera.getHalfWidth(), camera.getHalfHeight() );

	}

	private class EntityRenderPass implements IRenderingPass {

		@Override
		public void beginPass() {
			if ( shader == null ) {
				try {
					shader = Shader.makeShader( "plain.vs", "light.fs" );
				} catch ( SlickException e ) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			shader.setUniform2FVariable( "PlayerPos", camera.getHalfWidth(), camera.getHalfHeight() );
			shader.setUniformFVariable( "OuterSquaredDistance", 64 * 32 * 32 );
			shader.setUniformFVariable( "InnerSquaredDistance", 49 * 32 * 32 );
			shader.setUniformFVariable( "MinAlpha", 0 );
			shader.startShader();
		}

		@Override
		public void renderTile( final Graphics g, final Tile tile, final int worldX, final int worldY,
				final int screenX, final int screenY ) {

			if ( !fog.isExplored( worldX, worldY ) ) {
				return;
			}

			double distX = Math.pow( worldX - camera.getCenterX(), 2 );
			double distY = Math.pow( worldY - camera.getCenterY(), 2 );

			try {
				SpriteType spriteType = mapping.getSpriteType( tile.getTileType() );
				Image sprite = spriteManager.getSprite( spriteType );
				if ( sprite != null ) {

					if ( distX + distY <= 64 ) {

						Entity e = tile.getEntityOnTile();
						if ( e != null && e != gameState.getPlayer() ) {

							spriteType = e.getSpriteType();
							sprite = spriteManager.getSprite( spriteType, true );

							sprite.draw( screenX, screenY );

							// if ( e instanceof Monster ) {
							//
							// Monster actor = (Monster) e;
							// if ( actor.getCurrentHealth() <
							// actor.getMaxHealth() ) {
							// int width = (int) (20f * actor.getCurrentHealth()
							// / actor.getMaxHealth());
							//
							// g.drawString( "Monster", screenX, screenY );
							//
							// g.setColor( new org.newdawn.slick.Color( 0, 255,
							// 0, 255 ) );
							// g.fill( new Rectangle( screenX + 6, screenY - 2,
							// width, 4 ) );
							// g.drawRect( screenX + 6, screenY - 2, 20, 4 );
							// }
							// }
						}

					}
				}
			} catch ( IllegalArgumentException e ) {
				// unknown sprite
			}
		}

		@Override
		public void renderMissingTile( final Graphics g, final int worldX, final int worldY, final int screenX,
				final int screenY ) {
		}

		@Override
		public void endPass() {
			Shader.forceFixedShader();
		}

	}

	private class LightningComputationPass implements IRenderingPass {

		@Override
		public void beginPass() {
			if ( shader == null ) {
				try {
					shader = Shader.makeShader( "plain.vs", "light.fs" );
				} catch ( SlickException e ) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			updateFog( camera.getCenterX(), camera.getCenterY() );
			shader.setUniform2FVariable( "PlayerPos", camera.getHalfWidth(), camera.getHalfHeight() );
			shader.setUniformFVariable( "OuterSquaredDistance", 64 * 32 * 32 );
			shader.setUniformFVariable( "InnerSquaredDistance", 36 * 32 * 32 );
			shader.setUniformFVariable( "MinAlpha", 0.6f );
			shader.startShader();
			spriteManager.startUse();
		}

		@Override
		public void renderTile( final Graphics g, final Tile tile, final int worldX, final int worldY,
				final int screenX, final int screenY ) {

			if ( !fog.isExplored( worldX, worldY ) ) {
				return;
			}

			try {
				SpriteType spriteType = mapping.getSpriteType( tile.getTileType() );
				Image sprite = spriteManager.getSprite( spriteType );
				if ( sprite != null ) {
					spriteManager.renderInUse( spriteType, screenX, screenY );
				}
			} catch ( IllegalArgumentException e ) {
				// unknown sprite
			}
		}

		@Override
		public void renderMissingTile( final Graphics g, final int worldX, final int worldY, final int screenX,
				final int screenY ) {
		}

		@Override
		public void endPass() {
			spriteManager.endUse();
			Shader.forceFixedShader();
		}
	}

	private void updateFog( final float x, final float y ) {

		for ( int i = -8; i < 8; i++ ) {
			for ( int j = -8; j < 8; j++ ) {
				if ( Math.sqrt( i * i + j * j ) <= 8 ) {
					fog.setExplored( camera.worldToTile( x + i ), camera.worldToTile( y + j ), true );
				}
			}
		}
	}

}
