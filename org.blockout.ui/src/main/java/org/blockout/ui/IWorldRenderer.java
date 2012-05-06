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
	 * Renders the world around the current view center.
	 * 
	 * @param g
	 *            The Graphics instance.
	 */
	public abstract void render( final Graphics g );

}