package org.blockout.ui;

import org.newdawn.slick.Graphics;

/**
 * A world renderer is responsible for rendering the world around a given view
 * center.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public interface IWorldRenderer {

	/**
	 * Sets the center of the view that gets rendered by this class.
	 * 
	 * @param x
	 *            The x coordinate of the center relative to the world's origin.
	 *            E.g. 1.5f is in the mid of the tile with the x coordinate 1.
	 * @param y
	 *            The y coordinate of the center relative to the world's origin.
	 */
	public abstract void setViewCenter( final float x, final float y );

	/**
	 * Renders the world around the current view center.
	 * 
	 * @param g
	 *            The Graphics instance.
	 */
	public abstract void render( final Graphics g );

}