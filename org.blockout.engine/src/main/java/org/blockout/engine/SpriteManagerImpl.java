package org.blockout.engine;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import org.newdawn.slick.Image;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Implementation of a SpriteManager that provides access to the sprites of the
 * game and caches them
 * 
 * @author Florian Müller
 * 
 */
@Named
public class SpriteManagerImpl implements ISpriteManager {
	private static final int	CACHE_MAXSIZE			= 31;
	private static final int	CACHE_EXPIRE_TIME_MS	= 1000;

	ISpriteSheet				spriteSheet;
	SpriteMapping				spriteMapping;
	Cache<Integer, Image>		spriteCache;

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

		if ( !spriteSheet.isSpriteSheetLoaded() ) {
			throw new IllegalArgumentException( "spriteSheet has an invalid state" );
		}

		this.spriteSheet = spriteSheet;
		spriteMapping = new SpriteMapping();

		// Create a new cache for recently loaded sprites
		spriteCache = CacheBuilder.newBuilder().maximumSize( CACHE_MAXSIZE )
				.expireAfterAccess( CACHE_EXPIRE_TIME_MS, TimeUnit.MILLISECONDS ).build();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getSprite( final SpriteType type ) {
		Preconditions.checkNotNull( type );
		int spriteId;

		try {
			spriteId = spriteMapping.getSpriteId( type );
		} catch ( IllegalArgumentException e ) {
			// there was no sprite with this type
			return null;
		}
		
		Image sprite = spriteCache.getIfPresent(spriteId);
		
		if(null == sprite){
			sprite = spriteSheet.getSprite(spriteId);
			
			if(null != sprite)
				spriteCache.put(spriteId, sprite);
		}

		return sprite;
	}

}
