package org.blockout.engine;

import java.io.IOException;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import org.newdawn.slick.Image;

import com.google.common.base.Preconditions;

/**
 * Implementation of a SpriteManager that provides access to the sprites of the
 * game and caches them
 * 
 * @author Florian Müller
 * 
 */
@Named
public class SpriteManagerImpl implements ISpriteManager {

	ISpriteSheet							spriteSheet;
	SpriteMapping							spriteMapping;

	private final HashMap<String, Image>	sprites;

	/**
	 * Constructor to create a new SpriteManager with an internal cache
	 * 
	 * @param spriteSheet
	 *            Spritesheet to handle with this manager
	 * @throws IllegalArgumentException
	 *             If <i>spriteSheet</i> is not yet loaded.
	 */
	@Inject
	public SpriteManagerImpl(final ISpriteSheet spriteSheet) throws IllegalArgumentException {
		Preconditions.checkNotNull( spriteSheet );

		this.spriteSheet = spriteSheet;
		spriteMapping = new SpriteMapping();
		sprites = new HashMap<String, Image>();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getSprite( final SpriteType type ) {
		return getSprite( type, false );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getSprite( final SpriteType type, final boolean excludeBackground ) {
		Preconditions.checkNotNull( type );

		if ( !spriteSheet.isSpriteSheetLoaded() ) {
			loadDefaultSpriteSheet();
		}

		int spriteId;
		try {
			spriteId = spriteMapping.getSpriteId( type );
		} catch ( IllegalArgumentException e ) {
			// there was no sprite with this type
			return null;
		}

		String spriteKey = (excludeBackground) ? "nobg_" + spriteId : Integer.toString( spriteId );

		Image sprite = sprites.get( spriteKey );
		if ( null == sprite ) {
			sprite = spriteSheet.getSprite( spriteId );

			if ( null != sprite ) {
				if ( excludeBackground ) {
					sprite = Utils.exclude( getSprite( SpriteType.stoneground ), sprite );
				}
				sprites.put( spriteKey, sprite );
			}
		}

		return sprite;
	}
	
	/**
	 * @see ISpriteManager#startUse()
	 */
	@Override
	public void startUse() {
		if ( !spriteSheet.isSpriteSheetLoaded() ) {
			loadDefaultSpriteSheet();
		}
		
		spriteSheet.startUse();
	}

	/**
	 * @see ISpriteManager#endUse()
	 */
	@Override
	public void endUse() {
		spriteSheet.endUse();
	}

	/**
	 * @see ISpriteManager#renderInUse(SpriteType, int, int)
	 */
	@Override
	public void renderInUse( SpriteType type, int x, int y ) {
		Preconditions.checkNotNull(type);
		
		if ( !spriteSheet.isSpriteSheetLoaded() ) {
			loadDefaultSpriteSheet();
		}
		
		spriteSheet.renderInUse(spriteMapping.getSpriteId(type), x, y);
	}
	
	private void loadDefaultSpriteSheet(){
		try {
			spriteSheet.loadSpriteSheet( "nethack_spritesheet.jpg", 32, 32 );
		} catch ( IllegalArgumentException e ) {
			throw new RuntimeException( "Failed to load spritesheet.", e );
		} catch ( IOException e ) {
			throw new RuntimeException( "Failed to load spritesheet.", e );
		}
	}
}
