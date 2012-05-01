package org.blockout.engine;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;

import com.google.common.base.Preconditions;

/**
 * Utilities for graphical programming.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class Utils {

	/**
	 * Excludes the given background from the source image and sets the excluded
	 * pixels transparent. Note that this methods works with exact pixel
	 * matching.
	 * 
	 * @param background
	 *            Image of the background that gets excluded from the source.
	 * @param source
	 *            The image where the background should get excluded.
	 * @return The new image without the background.
	 * @throws IllegalArgumentException
	 *             If you pass in images with different bounds.
	 * @throws NullPointerException
	 *             If you pass in null for any argument.
	 */
	public static Image exclude( final Image background, final Image source ) {
		Preconditions.checkNotNull( background );
		Preconditions.checkNotNull( source );
		if ( background.getHeight() != source.getHeight() || background.getWidth() != source.getWidth() ) {
			throw new IllegalArgumentException( "Both images must have same bounds." );
		}

		ImageBuffer buffer = new ImageBuffer( source.getWidth(), source.getHeight() );
		for ( int y = 0; y < source.getHeight(); y++ ) {
			for ( int x = 0; x < source.getWidth(); x++ ) {
				Color color = source.getColor( x, y );
				if ( !areEqual( background.getColor( x, y ), color, 32 ) ) {
					buffer.setRGBA( x, y, color.getRedByte(), color.getGreenByte(), color.getBlueByte(),
							color.getAlphaByte() );
				}
			}
		}

		return buffer.getImage();
	}

	private static boolean areEqual( final Color c0, final Color c1, final int epsilon ) {
		if ( Math.abs( c0.getRedByte() - c1.getRedByte() ) > epsilon ) {
			return false;
		}
		if ( Math.abs( c0.getGreenByte() - c1.getGreenByte() ) > epsilon ) {
			return false;
		}
		if ( Math.abs( c0.getBlueByte() - c1.getBlueByte() ) > epsilon ) {
			return false;
		}
		if ( Math.abs( c0.getAlphaByte() - c1.getAlphaByte() ) > epsilon ) {
			return false;
		}
		return true;
	}
}
