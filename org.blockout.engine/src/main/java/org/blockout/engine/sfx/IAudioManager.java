package org.blockout.engine.sfx;

import org.newdawn.slick.Music;
import org.newdawn.slick.Sound;


/**
 * Implementations have to provide access to the sounds and music of the game.
 * 
 * @author Florian Müller
 *
 */
public interface IAudioManager {
	/**
	 * Returns the audio file  by the given type as a sound.
	 * 
	 * @param type Type of the audio file
	 * @return The audio file as a Sound identified by the type or null if the audio file is unknown.
	 * @throws NullPointerException If the type was null
	 */
	public Sound getSound(AudioType type);
	
	/**
	 * Returns the audio file  by the given type as music.
	 * 
	 * @param type Type of the audio file
	 * @return The audio file as Music identified by the type or null if the audio file is unknown.
	 * @throws NullPointerException If the type was null
	 */
	public Music getMusic(AudioType type);
}
