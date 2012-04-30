package org.blockout.engine;

import java.io.File;
import java.io.IOException;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.google.common.base.Preconditions;

/**
 * Implementation of a SpriteSheet to access single sprites inside an image 
 * by its id
 * 
 * @author Florian Müller
 *
 */
public class SpriteSheetImpl implements ISpriteSheet {
	private SpriteSheet spriteSheet;
	private int numSprites;
	
	/**
	 * Constructor to create an empty SpriteSheet
	 */
	public SpriteSheetImpl() {
		spriteSheet = null;
		numSprites = -1;
	}
	
	/**
	 * Constructor to create a new SpriteSheet from an image source with a 
	 * specific tile size
	 * 
	 * @param ref The location of the spritesheet file to load
	 * @param tw Tile width
	 * @param th Tile height
	 * @throws IOException If there is a problem while loading the spritesheet
	 * @throws IllegalArgumentException If <i>tw</i> or <i>th</i> have wrong values
	 */
	public SpriteSheetImpl(String ref, int tw, int th) throws IOException, IllegalArgumentException {
		loadSpriteSheet(ref, tw, th);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loadSpriteSheet(String ref, int tw, int th) throws IOException, IllegalArgumentException {
		Preconditions.checkNotNull(ref);
		
		if(0 > tw)
			throw new IllegalArgumentException("Tile width has a negative value");
		
		if(0 > th)
			throw new IllegalArgumentException("Tile height has a negative value");
		
		if(!new File(ref).exists())
			throw new IOException("Resource not found: " + ref);
		
		Image img;
		try {
			img = new Image(ref);
		} catch (SlickException e) {
			throw new IOException(e);
		}
		
		if(img.getWidth() < tw)
			throw new IllegalArgumentException("Tile width is to big");
		
		if(img.getHeight() < th)
			throw new IllegalArgumentException("Tile height is to big");
	
		spriteSheet = new SpriteSheet(img, tw, th);
		numSprites = spriteSheet.getVerticalCount() + spriteSheet.getHorizontalCount();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSpriteSheetLoaded() {
		return null != spriteSheet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getSprite(int spriteId) throws IllegalStateException {
		if (null == spriteSheet)
			throw new IllegalStateException("No spritesheet loaded");
		
		if (0 < spriteId && numSprites > spriteId) {
			int sx = spriteId % spriteSheet.getHorizontalCount();
			int sy = spriteId / spriteSheet.getVerticalCount();
			return spriteSheet.getSprite(sx, sy);
		}
		
		return null;
	}
}
