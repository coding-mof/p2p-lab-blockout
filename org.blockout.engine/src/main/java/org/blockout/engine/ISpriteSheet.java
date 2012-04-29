package org.blockout.engine;

import java.io.IOException;

import org.newdawn.slick.Image;

/**
 * Interface for a SpriteManager that loads a spritesheet
 * and give other the possibility to get a single sprite
 * by its id from this spritesheet.
 * 
 * @author Florian Müller
 */
public interface ISpriteSheet {
	/**
	 * 
	 * @param ref The location of the spritesheet file to load
	 * @param tw Tile width
	 * @param th Tile height
	 * @throws IOException Thrown if there is a problem while loading the spritesheet
	 */
	public void loadSpriteSheet(String ref, int tw, int th) throws IOException;
	
	/**
	 * Check if there is a spritesheet loaded
	 * 
	 * @return Returns true if there is a loaded spritesheet, false otherwise
	 */
	public boolean isSpriteSheetLoaded();
	
	/**
	 * Returns a sprite from the spritesheet
	 * 
	 * @param spriteId Id of the sprite
	 * @return Image of the requested sprite or null if there is no sprite with this id
	 * @throws IllegalStateException Thrown if there is no spritesheed loaded
	 */
	public Image getSprite(int spriteId) throws IllegalStateException;
}
