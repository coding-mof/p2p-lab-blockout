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
			case tiger:	
				return 40;
			case gremlin:
				return 41;
			case gargoyle:	
				return 42;	
			case winged_gargoyle:
				return 43;		
			case hobbit:
				return 44;
			case dwarf:	
				return 45;
			case bugbear:	
				return 46;	
			case dwarf_lord:
				return 47;
			case dwarf_king:
				return 48;	
			case mind_flayer:
				return 49;		
			case master_mind_flayer:
				return 50;
			case manes:	
				return 51;
			case homunculus:	
				return 52;	
			case imp:
				return 53;
			case lemure:	
				return 54;	
			case quasit:	
				return 55;
			case tengu:
				return 56;
			case blue_jelly:
				return 57;		
			case spotted_jelly:	
				return 58;	
			case ochre_jelly:
				return 59;
			case kobold:	
				return 60;
			case large_kobold:	
				return 61;	
			case kobold_lord:
				return 62;
			case kobold_shaman:	
				return 63;	
			case leprechaun:
				return 64;
			case small_mimic:
				return 65;
			case large_mimic:	
				return 66;
			case giant_mimic:	
				return 67;
			case wood_nymph:
				return 68;
			case water_nymph:	
				return 69;	
			case mountain_nymph:
				return 70;		
			case goblin:
				return 71;
			case hobgoblin:	
				return 72;	
			case orc:
				return 73;
			case hill_orc:
				return 74;
			case Mordor_orc:	
				return 75;	
			case Uruk_hai:	
				return 76;
			case orc_shaman:
				return 77;
			case orc_captain:	
				return 78;	
			case rock_piercer:
				return 79;
				
				
			// Objects
			case arrow:
				return 395;
			case elven_arrow:
				return 396;
			case orcish_arrow:
				return 397;
			case silver_arrow:	
				return 398;
			case ya:
				return 399;
			case crossbow_bolt:	
				return 400;
			case dart:	
				return 401;
			case shuriken:	
				return 402;
			case boomerang:	
				return 403;		
			case spear:	
				return 404;
			case elven_spear:	
				return 405;
			case orcish_spear:	
				return 406;		
			case dwarvish_spear:	
				return 407;		
			case silver_spear:	
				return 408;
			case javelin:	
				return 409;
			case trident:		
				return 410;
			case dagger:	
				return 411;
			case elven_dagger:	
				return 412;		
			case orcish_dagger:	
				return 413;		
			case silver_dagger:	
				return 414;
			case athame:	
				return 415;
			case scalpel:		
				return 416;	
			case knife:	
				return 417;
			case stiletto:		
				return 418;	
			case worm_tooth:	
				return 419;		
			case crysknife:	
				return 420;
			case axe:	
				return 421;
			case battle_axe:		
				return 422;	
			case short_sword:	
				return 423;
			case elven_sword:	
				return 424;
			case sword:	
				return 425;
			case orcish_short_sword:	
				return 426;		
			case dwarvish_short: 	
				return 427;
			case silver_saber:		
				return 428;
			case broadsword:	
				return 429;
			case elven_broadsword:	
				return 430;		
			case long_sword:		
				return 431;
			case two_handed_sword:	
				return 432;
			case katana:	
				return 433;
			case tsurugi:		
				return 434;	
			case runesword:	
				return 435;
			case partisan:	
				return 436;	
			case ranseur:	
				return 437;	
			case spetum:	
				return 438;
			case glaive:
				return 439;
			
		
			
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
			case 40:	
				return SpriteType.tiger;
			case 41:
				return SpriteType.gremlin;
			case 42:	
				return SpriteType.gargoyle;	
			case 43:
				return SpriteType.winged_gargoyle;		
			case 44:
				return SpriteType.hobbit;
			case 45:	
				return SpriteType.dwarf;
			case 46:	
				return SpriteType.bugbear;	
			case 47:
				return SpriteType.dwarf_lord;
			case 48:
				return SpriteType.dwarf_king;	
			case 49:
				return SpriteType.mind_flayer;		
			case 50:
				return SpriteType.master_mind_flayer;
			case 51:	
				return SpriteType.manes;
			case 52:	
				return SpriteType.homunculus;	
			case 53:
				return SpriteType.imp;
			case 54:	
				return SpriteType.lemure;	
			case 55:	
				return SpriteType.quasit;
			case 56:
				return SpriteType.tengu;
			case 57:
				return SpriteType.blue_jelly;		
			case 58:	
				return SpriteType.spotted_jelly;	
			case 59:
				return SpriteType.ochre_jelly;
			case 60:	
				return SpriteType.kobold;
			case 61:	
				return SpriteType.large_kobold;	
			case 62:
				return SpriteType.kobold_lord;
			case 63:	
				return SpriteType.kobold_shaman;	
			case 64:
				return SpriteType.leprechaun;
			case 65:
				return SpriteType.small_mimic;
			case 66:	
				return SpriteType.large_mimic;
			case 67:	
				return SpriteType.giant_mimic;
			case 68:
				return SpriteType.wood_nymph;
			case 69:	
				return SpriteType.water_nymph;	
			case 70:
				return SpriteType.mountain_nymph;		
			case 71:
				return SpriteType.goblin;
			case 72:	
				return SpriteType.hobgoblin;	
			case 73:
				return SpriteType.orc;
			case 74:
				return SpriteType.hill_orc;
			case 75:	
				return SpriteType.Mordor_orc;	
			case 76:	
				return SpriteType.Uruk_hai;
			case 77:
				return SpriteType.orc_shaman;
			case 78:	
				return SpriteType.orc_captain;	
			case 79:
				return SpriteType.rock_piercer;
				
			// Objects
			case 395:
				return SpriteType.arrow;
			case 396:
				return SpriteType.elven_arrow;
			case 397:
				return SpriteType.orcish_arrow;
			case 398:	
				return SpriteType.silver_arrow;
			case 399:
				return SpriteType.ya;
			case 400:	
				return SpriteType.crossbow_bolt;
			case 401:	
				return SpriteType.dart;
			case 402:	
				return SpriteType.shuriken;
			case 403:	
				return SpriteType.boomerang;		
			case 404:	
				return SpriteType.spear;
			case 405:	
				return SpriteType.elven_spear;
			case 406:	
				return SpriteType.orcish_spear;		
			case 407:	
				return SpriteType.dwarvish_spear;		
			case 408:	
				return SpriteType.silver_spear;
			case 409:	
				return SpriteType.javelin;
			case 410:		
				return SpriteType.trident;
			case 411:	
				return SpriteType.dagger;
			case 412:	
				return SpriteType.elven_dagger;		
			case 413:	
				return SpriteType.orcish_dagger;		
			case 414:	
				return SpriteType.silver_dagger;
			case 415:	
				return SpriteType.athame;
			case 416:		
				return SpriteType.scalpel;	
			case 417:	
				return SpriteType.knife;
			case 418:		
				return SpriteType.stiletto;	
			case 419:	
				return SpriteType.worm_tooth;		
			case 420:	
				return SpriteType.crysknife;
			case 421:	
				return SpriteType.axe;
			case 422:		
				return SpriteType.battle_axe;	
			case 423:	
				return SpriteType.short_sword;
			case 424:	
				return SpriteType.elven_sword;
			case 425:	
				return SpriteType.sword;
			case 426:	
				return SpriteType.orcish_short_sword;		
			case 427: 	
				return SpriteType.dwarvish_short;
			case 428:		
				return SpriteType.silver_saber;
			case 429:	
				return SpriteType.broadsword;
			case 430:	
				return SpriteType.elven_broadsword;		
			case 431:		
				return SpriteType.long_sword;
			case 432:	
				return SpriteType.two_handed_sword;
			case 433:	
				return SpriteType.katana;
			case 434:		
				return SpriteType.tsurugi;	
			case 435:	
				return SpriteType.runesword;
			case 436:	
				return SpriteType.partisan;	
			case 437:	
				return SpriteType.ranseur;	
			case 438:	
				return SpriteType.spetum;
			case 439:
				return SpriteType.glaive;			
			
			// structures
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
