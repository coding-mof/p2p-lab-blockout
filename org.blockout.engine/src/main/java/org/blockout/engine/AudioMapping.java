package org.blockout.engine;

import com.google.common.base.Preconditions;


/**
 * Mapping between audio types and location of the corresponding audio file
 * 
 * @author Florian Müller
 *
 */
public class AudioMapping {
	
	/**
	 * Returns the location of an audio type.
	 * 
	 * @param type
	 *            The type of the sprite.
	 * @return The id of the sprite.
	 * @throws IllegalArgumentException
	 *             If you pass in an unknown sprite type.
	 * @throws NullPointerException
	 *             If you pass in null.
	 */
	public String getAudioRef(AudioType type) throws IllegalArgumentException{
		Preconditions.checkNotNull(type);
		
		switch (type) {
			// ...
		default:
			throw new IllegalArgumentException( "Unknown audio type: " + type );
		}
	}
}
