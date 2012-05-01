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

	public static final int	EXCLUDE_EPSILON	= 32;

	/**
	 * Excludes the given background from the source image and sets the excluded
	 * pixels transparent.
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
				if ( !areEqual( background.getColor( x, y ), color, EXCLUDE_EPSILON ) ) {
					buffer.setRGBA( x, y, color.getRedByte(), color.getGreenByte(), color.getBlueByte(),
							color.getAlphaByte() );
				}
			}
		}

		return buffer.getImage();
	}

	/**
	 * Compares two colors. The colors are assumed to be eaqual when the
	 * difference of each channel is lower than the given epsilon.
	 * 
	 * @param c0
	 *            The first color.
	 * @param c1
	 *            The second color.
	 * @param epsilon
	 *            The maximum difference both colors can have on each channel to
	 *            be treated as equal. Valid range is from 0 (perfect matching)
	 *            up to 255 (match any).
	 * @return If the difference of each channel of both colors is lower than
	 *         the given epsilon.
	 * @throws IllegalArgumentException
	 *             If epsilon is not in the range of 0 up to 255.
	 * @throws NullPointerException
	 *             If any given color argument is null.
	 */
	public static boolean areEqual( final Color c0, final Color c1, final int epsilon ) {
		Preconditions.checkNotNull( c0 );
		Preconditions.checkNotNull( c1 );
		if ( epsilon < 0 || epsilon > 255 ) {
			throw new IllegalArgumentException( "Epsilon must be in the range of 0 up to 255." );
		}
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
