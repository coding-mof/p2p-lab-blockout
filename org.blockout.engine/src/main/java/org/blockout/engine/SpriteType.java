package org.blockout.engine;

/**
 * Convenient enumeration for specifying the type of a tile.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public enum SpriteType {
	Crate, 
	Player,
	Skeleton,
	Zombie,
	
	// Monters
	giant_ant, killer_bee, soldier_ant, fire_ant, giant_beetle,		
	queen_bee, acid_blob, quivering_blob, gelatinous_cube,
	chickatrice, cockatrice, pyrolisk, jackal, fox,
	coyote, werejackal, little_dog, dog, large_dog,		
	dingo, wolf, werewolf, warg, winter_wolf_cub,
	winter_wolf, hell_hound_pup, hell_hound, Cerberus, gas_spore,		
	floating_eye, freezing_sphere, flaming_sphere, shocking_sphere,
	beholder, kitten, housecat, jaguar, lynx,
	panther,large_cat,
	
	
	// Structures
	wallway_vertical, wallway_horizontal, wallway_corner_up_tight,
	wallway_corner_up_left, wallway_corner_down_right,
	wallway_corner_down_left, wallway_crossing,
	wallway_intersection_horizontal_up, wallway_intersection_horizontal_down,
	wallway_intersection_vertical_right, wallway_intersection_vertical_left,
	
	open_door,closed_door,iron_bars,tree,
	stoneground,stoneground_dark,stoneground_mid,
	staircase_up,staircase_down,ladder_up,
	ladder_down,altar,grave,opulent_throne,		
	sink,fountain,water,ice,molten_lava,
	lowered_drawbridge_vertical,
	lowered_drawbridge_horizontal,		
	raised_drawbridge_vertical,
	raised_drawbridge_horizontal,
	
}
