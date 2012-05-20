package org.blockout.engine.sfx;

import java.util.HashMap;

import javax.inject.Named;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import com.google.common.base.Preconditions;

/**
 * Implementation of an AudioManager to provide access to the sounds and music
 * of the game. Sound and Music objects are cached
 * 
 * @author Florian Müller
 * 
 */
@Named
public class AudioManagerImpl implements IAudioManager {
	private final HashMap<AudioType, Sound>	soundCache;
	private final HashMap<AudioType, Music>	musicCache;
	private final AudioMapping				audioMapping;

	/**
	 * Contructor to create an empty AudioManager
	 */
	public AudioManagerImpl() {
		audioMapping = new AudioMapping();

		soundCache = new HashMap<AudioType, Sound>();
		musicCache = new HashMap<AudioType, Music>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Sound getSound( final AudioType type ) {
		Preconditions.checkNotNull( type );
		String ref;

		try {
			ref = audioMapping.getAudioRef( type );
		} catch ( IllegalArgumentException e ) {
			return null;
		}

		if ( !soundCache.containsKey( type ) ) {
			Sound sound;
			try {
				sound = new Sound( ref );
			} catch ( SlickException e ) {
				throw new RuntimeException( "failed to load sound from " + ref, e );
			}

			soundCache.put( type, sound );
		}

		return soundCache.get( type );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Music getMusic( final AudioType type ) {
		Preconditions.checkNotNull( type );
		String ref;

		try {
			ref = audioMapping.getAudioRef( type );
		} catch ( IllegalArgumentException e ) {
			return null;
		}

		if ( !musicCache.containsKey( type ) ) {
			Music music;
			try {
				music = new Music( ref );
			} catch ( SlickException e ) {
				throw new RuntimeException( "failed to load music from " + ref, e );
			}

			musicCache.put( type, music );
		}

		return musicCache.get( type );
	}
}
