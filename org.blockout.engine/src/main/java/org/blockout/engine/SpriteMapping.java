package org.blockout.engine;

import com.google.common.base.Preconditions;

/**
 * Mapping between sprite types and sprite ids used by the {@link ISpriteSheet}.
 * 
 * @author Marc-Christian Schulze
 * @author Florian Müller
 * 
 */
public class SpriteMapping {

	/**
	 * Returns the id of a sprite type.
	 * 
	 * @param type
	 *            The type of the sprite.
	 * @return The id of the sprite.
	 * @throws IllegalArgumentException
	 *             If you pass in an unknown sprite type.
	 * @throws NullPointerException
	 *             If you pass in null.
	 */
	public int getSpriteId( final SpriteType type ) {
		Preconditions.checkNotNull( type );
		switch ( type ) {

			case Player:
				return 245;
			case Crate:
				return 586;
			case Zombie:
				return 249;
			case Skeleton:
				return 250;

				// Monsters
			case giant_ant:
				return 0;
			case killer_bee:
				return 1;
			case soldier_ant:
				return 2;
			case fire_ant:
				return 3;
			case giant_beetle:
				return 3;
			case queen_bee:
				return 4;
			case acid_blob:
				return 5;
			case quivering_blob:
				return 6;
			case gelatinous_cube:
				return 7;
			case chickatrice:
				return 8;
			case cockatrice:
				return 9;
			case pyrolisk:
				return 10;
			case jackal:
				return 11;
			case fox:
				return 12;
			case coyote:
				return 13;
			case werejackal:
				return 14;
			case little_dog:
				return 15;
			case dog:
				return 16;
			case large_dog:
				return 17;
			case dingo:
				return 18;
			case wolf:
				return 19;
			case werewolf:
				return 20;
			case warg:
				return 21;
			case winter_wolf_cub:
				return 22;
			case winter_wolf:
				return 23;
			case hell_hound_pup:
				return 24;
			case hell_hound:
				return 25;
			case Cerberus:
				return 26;
			case gas_spore:
				return 27;
			case floating_eye:
				return 28;
			case freezing:
				return 29;
			case sphere:
				return 30;
			case flaming_sphere:
				return 31;
			case shocking_sphere:
				return 32;
			case beholder:
				return 33;
			case kitten:
				return 34;
			case housecat:
				return 35;
			case jaguar:
				return 36;
			case lynx:
				return 37;
			case panther:
				return 38;
			case large_cat:
				return 39;

				// Structures

			case wallway_vertical:
				return 830;
			case wallway_horizontal:
				return 831;
			case wallway_corner_up_tight:
				return 832;
			case wallway_corner_up_left:
				return 833;
			case wallway_corner_down_right:
				return 834;
			case wallway_corner_down_left:
				return 835;
			case wallway_crossing:
				return 836;
			case wallway_intersection_horizontal_up:
				return 837;
			case wallway_intersection_horizontal_down:
				return 838;
			case wallway_intersection_vertical_right:
				return 839;
			case wallway_intersection_vertical_left:
				return 840;
			case open_door:
				return 843;
			case closed_door:
				return 845;
			case iron_bars:
				return 846;
			case tree:
				return 847;
			case StoneGround:
			case stoneground:
				return 848;
			case stoneground_dark:
				return 849;
			case stoneground_mid:
				return 850;
			case staircase_up:
				return 851;
			case staircase_down:
				return 852;
			case ladder_up:
				return 853;
			case ladder_down:
				return 854;
			case altar:
				return 856;
			case grave:
				return 857;
			case opulent_throne:
				return 858;
			case sink:
				return 859;
			case fountain:
				return 860;
			case water:
				return 861;
			case ice:
				return 862;
			case molten_lava:
				return 863;
			case lowered_drawbridge_vertical:
				return 864;
			case lowered_drawbridge_horizontal:
				return 865;
			case raised_drawbridge_vertical:
				return 866;
			case raised_drawbridge_horizontal:
				return 867;

			default:
				throw new IllegalArgumentException( "Unknown sprite type: " + type );
		}
	}

	public SpriteType getSpriteType( final int id ) {

		switch ( id ) {
			case 245:
				return SpriteType.Player;

				// World
			case 848:
				return SpriteType.StoneGround;

			case 849:
				return SpriteType.stoneground_dark;
			case 850:
				return SpriteType.stoneground_mid;
			case 586:
				return SpriteType.Crate;

				// Monsters
			case 249:
				return SpriteType.Zombie;
			case 250:
				return SpriteType.Skeleton;

			default:
				throw new IllegalArgumentException( "Unknown sprite id: " + id );
		}
	}
}
