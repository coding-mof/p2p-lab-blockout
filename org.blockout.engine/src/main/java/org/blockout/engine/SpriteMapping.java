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
				return 4;	
			case queen_bee: 
				return 5;
			case acid_blob:
				return 6; 
			case quivering_blob:
				return 7; 
			case gelatinous_cube:
				return 8;
			case chickatrice:
				return 9; 
			case cockatrice:
				return 10; 
			case pyrolisk:
				return 11; 
			case jackal: 
				return 12;
			case fox:
				return 13;
			case coyote: 
				return 14;
			case werejackal:
				return 15; 
			case little_dog:
				return 16; 
			case dog:
				return 17; 
			case large_dog:
				return 18;		
			case dingo: 
				return 19;
			case wolf: 
				return 20;
			case werewolf: 
				return 21;
			case warg: 
				return 22;
			case winter_wolf_cub:
				return 23;
			case winter_wolf: 
				return 24;
			case hell_hound_pup:
				return 25; 
			case hell_hound:
				return 26; 
			case Cerberus:
				return 27; 
			case gas_spore:
				return 28;
			case floating_eye:
				return 29; 
			case freezing_sphere: 
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
				return 855;
			case grave:		
				return 856;
			case opulent_throne:
				return 857;		
			case sink:
				return 858;
			case fountain:	
				return 859;	
			case water:	
				return 860;
			case ice:
				return 861;
			case molten_lava:
				return 862;
			case lowered_drawbridge_vertical:
				return 863;
			case lowered_drawbridge_horizontal:	
				return 864;	
			case raised_drawbridge_vertical:
				return 865;
			case raised_drawbridge_horizontal:
				return 866;

			default:
				throw new IllegalArgumentException( "Unknown sprite type: " + type );
		}
	}

	public SpriteType getSpriteType( final int id ) {

		switch ( id ) {
			case 245:
				return SpriteType.Player;

				// World
			case 586:
				return SpriteType.Crate;

				// Monsters
			case 249:
				return SpriteType.Zombie;
			case 250:
				return SpriteType.Skeleton;
				
				
			case 0: 
				return SpriteType.giant_ant;
			case 1:
				return SpriteType.killer_bee; 
			case 2:
				return SpriteType.soldier_ant; 
			case 3: 
				return SpriteType.fire_ant;
			case 4:	
				return SpriteType.giant_beetle;	
			case 5: 
				return SpriteType.queen_bee;
			case 6:
				return SpriteType.acid_blob; 
			case 7:
				return SpriteType.quivering_blob; 
			case 8:
				return SpriteType.gelatinous_cube;
			case 9:
				return SpriteType.chickatrice; 
			case 10:
				return SpriteType.cockatrice; 
			case 11:
				return SpriteType.pyrolisk; 
			case 12: 
				return SpriteType.jackal;
			case 13:
				return SpriteType.fox;
			case 14: 
				return SpriteType.coyote;
			case 15:
				return SpriteType.werejackal; 
			case 16:
				return SpriteType.little_dog; 
			case 17:
				return SpriteType.dog; 
			case 18:
				return SpriteType.large_dog;		
			case 19: 
				return SpriteType.dingo;
			case 20: 
				return SpriteType.wolf;
			case 21: 
				return SpriteType.werewolf;
			case 22: 
				return SpriteType.warg;
			case 23:
				return SpriteType.winter_wolf_cub;
			case 24: 
				return SpriteType.winter_wolf;
			case 25:
				return SpriteType.hell_hound_pup; 
			case 26:
				return SpriteType.hell_hound; 
			case 27:
				return SpriteType.Cerberus; 
			case 28:
				return SpriteType.gas_spore;
			case 29:
				return SpriteType.floating_eye;
			case 30: 
				return SpriteType.freezing_sphere;
			case 31:
				return SpriteType.flaming_sphere; 
			case 32:
				return SpriteType.shocking_sphere;
			case 33: 
				return SpriteType.beholder;
			case 34: 
				return SpriteType.kitten;
			case 35: 
				return SpriteType.housecat;
			case 36: 
				return SpriteType.jaguar;
			case 37:
				return SpriteType.lynx;
			case 38:
				return SpriteType.panther;
			case 39:
				return SpriteType.large_cat;
			case 830: 
				return SpriteType.wallway_vertical;
			case 831: 
				return SpriteType.wallway_horizontal;
			case 832:
				return SpriteType.wallway_corner_up_tight;
			case 833: 
				return SpriteType.wallway_corner_up_left;
			case 834:
				return SpriteType.wallway_corner_down_right;
			case 835: 
				return SpriteType.wallway_corner_down_left;
			case 836:
				return SpriteType.wallway_crossing;
			case 837: 
				return SpriteType.wallway_intersection_horizontal_up;
			case 838:
				return SpriteType.wallway_intersection_horizontal_down;
			case 839:
				return SpriteType.wallway_intersection_vertical_right;
			case 840:
				return SpriteType.wallway_intersection_vertical_left;
			case 843:	
				return SpriteType.open_door;	
			case 845:
				return SpriteType.closed_door;		
			case 846:	
				return SpriteType.iron_bars;
			case 847:
				return SpriteType.tree;
			case 848:
				return SpriteType.stoneground;
			case 849:
				return SpriteType.stoneground_dark;
			case 850:
				return SpriteType.stoneground_mid;
			case 851:
				return SpriteType.staircase_up;		
			case 852:
				return SpriteType.staircase_down;		
			case 853:
				return SpriteType.ladder_up;
			case 854:
				return SpriteType.ladder_down;
			case 855:
				return SpriteType.altar;
			case 856:		
				return SpriteType.grave;
			case 857:
				return SpriteType.opulent_throne;		
			case 858:
				return SpriteType.sink;
			case 859:	
				return SpriteType.fountain;	
			case 860:	
				return SpriteType.water;
			case 861:
				return SpriteType.ice;
			case 862:
				return SpriteType.molten_lava;
			case 863:
				return SpriteType.lowered_drawbridge_vertical;
			case 864:	
				return SpriteType.lowered_drawbridge_horizontal;	
			case 865:
				return SpriteType.raised_drawbridge_vertical;
			case 866:
				return SpriteType.raised_drawbridge_horizontal;

			default:
				throw new IllegalArgumentException( "Unknown sprite id: " + id );
		}
	}
}
