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
	public String getAudioRef( final AudioType type ) throws IllegalArgumentException {
		Preconditions.checkNotNull( type );

		switch ( type ) {
			case sfx_swing1:
				return "sfx/swing.wav";
			case sfx_swing2:
				return "sfx/swing2.wav";
			case sfx_swing3:
				return "sfx/swing3.wav";
			case sfx_sword_clash1:
				return "sfx/sword-unsheathe.wav";
			case sfx_sword_clash2:
				return "sfx/sword-unsheathe2.wav";
			case sfx_sword_clash3:
				return "sfx/sword-unsheathe3.wav";
			case sfx_sword_clash4:
				return "sfx/sword-unsheathe4.wav";
			case sfx_sword_clash5:
				return "sfx/sword-unsheathe5.wav";
			case sfx_stonestep_fast:
				return "sfx/stonestep_fast.wav";
			case sfx_stonestep1:
				return "sfx/stonestep.wav";
			case sfx_stonestep2:
				return "sfx/stonestep2.wav";
			case sfx_stonestep3:
				return "sfx/stonestep2.wav";
			case sfx_open_chest:
				return "sfx/open-chest.wav";
				
			case music_irish_meadow:
				return "ambient/Marc_Teichert_-_Irish_Meadow.ogg";
			default:
				throw new IllegalArgumentException( "Unknown audio type: " + type );
		}
	}
}
