package org.blockout.engine.sfx;


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
		case swing1:
			return "sfx/swing.wav";
		case swing2:
			return "sfx/swing2.wav";
		case swing3:
			return "sfx/swing3.wav";
		case sword_clash1:
			return "sfx/sword-unsheathe.wav";
		case sword_clash2:
			return "sfx/sword-unsheathe.wav2";
		case sword_clash3:
			return "sfx/sword-unsheathe.wav3";
		case sword_clash4:
			return "sfx/sword-unsheathe.wav4";
		case sword_clash5:
			return "sfx/sword-unsheathe.wav5";
		case stonestep_fast:
			return "sfx/stonestep_fast.wav";
		case stonestep:
			return "sfx/stonestep.wav";
		default:
			throw new IllegalArgumentException( "Unknown audio type: " + type );
		}
	}
}
