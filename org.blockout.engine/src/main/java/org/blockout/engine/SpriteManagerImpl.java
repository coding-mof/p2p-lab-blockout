package org.blockout.engine;

import java.io.IOException;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import org.newdawn.slick.Image;

import com.google.common.base.Preconditions;

import de.lessvoid.nifty.slick2d.render.image.ImageSlickRenderImage;
import de.lessvoid.nifty.slick2d.render.image.SlickLoadImageException;
import de.lessvoid.nifty.slick2d.render.image.SlickRenderImage;
import de.lessvoid.nifty.slick2d.render.image.loader.SlickRenderImageLoader;

/**
 * Implementation of a SpriteManager that provides access to the sprites of the
 * game and caches them
 * 
 * @author Florian Müller
 * 
 */
@Named
public class SpriteManagerImpl implements ISpriteManager{

	ISpriteSheet							spriteSheet;
	SpriteMapping							spriteMapping;

	private final HashMap<Integer, Image>	sprites;

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
		sprites = new HashMap<Integer, Image>();

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

		int spriteKey = spriteId * 10;
		if ( excludeBackground ) {
			spriteKey++;
		}

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
	 * Load a sprite to use it in nifty GUI	 
	 *
	 * @throws NullPointerException
	 * 		If filename is null
	 * @throws SlickLoadImageException
	 * 		If there is a problem while loading the image
	 */
	@Override
	public SlickRenderImage loadImage( final String filename, final boolean filterLinear )
			throws SlickLoadImageException {
		Preconditions.checkNotNull(filename);
		SpriteType type;
		
		try{
			type = SpriteType.valueOf(filename);
		}
		catch (IllegalArgumentException e) {
			throw new SlickLoadImageException("There is no image with the name '" + filename + "'");
		}
		
		Image image = getSprite(type, true);
		
		if(null != image)
			return new ImageSlickRenderImage(image);
		
		throw new SlickLoadImageException("Failed to load image with the name '" + filename + "'");
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
	public void renderInUse( final SpriteType type, final int x, final int y ) {
		Preconditions.checkNotNull( type );

		if ( !spriteSheet.isSpriteSheetLoaded() ) {
			loadDefaultSpriteSheet();
		}

		spriteSheet.renderInUse( spriteMapping.getSpriteId( type ), x, y );
	}

	private void loadDefaultSpriteSheet() {
		try {
			spriteSheet.loadSpriteSheet( "nethack_spritesheet.jpg", 32, 32 );
		} catch ( IllegalArgumentException e ) {
			throw new RuntimeException( "Failed to load spritesheet.", e );
		} catch ( IOException e ) {
			throw new RuntimeException( "Failed to load spritesheet.", e );
		}
	}
}
