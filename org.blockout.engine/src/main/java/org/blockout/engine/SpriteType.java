package org.blockout.engine;

/**
 * Convenient enumeration for specifying the type of a tile.
 * 
 * @author Marc-Christian Schulze
 * @author Florian Müller
 * 
 */
public enum SpriteType {
	Crate, Player, Skeleton, Zombie,

	// Monters
	giant_ant, killer_bee, soldier_ant, fire_ant, giant_beetle, queen_bee, acid_blob, quivering_blob, gelatinous_cube, chickatrice, cockatrice, pyrolisk, jackal, fox, coyote, werejackal, little_dog, dog, large_dog, dingo, wolf, werewolf, warg, winter_wolf_cub, winter_wolf, hell_hound_pup, hell_hound, Cerberus, gas_spore, floating_eye, freezing_sphere, flaming_sphere, shocking_sphere, beholder, kitten, housecat, jaguar, lynx, panther, large_cat, tiger, gremlin, gargoyle, winged_gargoyle, hobbit, dwarf, bugbear, dwarf_lord, dwarf_king, mind_flayer, master_mind_flayer, manes, homunculus, imp, lemure, quasit, tengu, blue_jelly, spotted_jelly, ochre_jelly, kobold, large_kobold, kobold_lord, kobold_shaman, leprechaun, small_mimic, large_mimic, giant_mimic, wood_nymph, water_nymph, mountain_nymph, goblin, hobgoblin, orc, hill_orc, Mordor_orc, Uruk_hai, orc_shaman, orc_captain, rock_piercer,

	// objects
	arrow, elven_arrow, orcish_arrow, silver_arrow, ya, crossbow_bolt, dart, shuriken, boomerang, spear, elven_spear, orcish_spear, dwarvish_spear, silver_spear, javelin, trident, dagger, elven_dagger, orcish_dagger, silver_dagger, athame, scalpel, knife, stiletto, worm_tooth, crysknife, axe, battle_axe, short_sword, elven_sword, sword, orcish_short_sword, dwarvish_short, silver_saber, broadsword, elven_broadsword, long_sword, two_handed_sword, katana, tsurugi, runesword, partisan, ranseur, spetum, glaive,

	// Structures
	wallway_vertical, wallway_horizontal, wallway_corner_up_tight, wallway_corner_up_left, wallway_corner_down_right, wallway_corner_down_left, wallway_crossing, wallway_intersection_horizontal_up, wallway_intersection_horizontal_down, wallway_intersection_vertical_right, wallway_intersection_vertical_left,

	open_door, closed_door, iron_bars, tree, stoneground, stoneground_dark, stoneground_mid, staircase_up, staircase_down, ladder_up, ladder_down, altar, grave, opulent_throne, sink, fountain, water, ice, molten_lava, lowered_drawbridge_vertical, lowered_drawbridge_horizontal, raised_drawbridge_vertical, raised_drawbridge_horizontal,

}
