package org.blockout.ai;

import java.util.Random;

import javax.inject.Inject;

import org.blockout.common.TileCoordinate;
import org.blockout.engine.Camera;
import org.blockout.ui.LocalPlayerMoveHandler;
import org.blockout.world.IWorld;
import org.blockout.world.LocalGameState;
import org.blockout.world.Tile;
import org.blockout.world.entity.Actor;
import org.blockout.world.entity.Crate;
import org.blockout.world.entity.Entity;
import org.blockout.world.entity.Player;
import org.blockout.world.items.Armor;
import org.blockout.world.items.Elixir;
import org.blockout.world.items.Gloves;
import org.blockout.world.items.Helm;
import org.blockout.world.items.Shield;
import org.blockout.world.items.Shoes;
import org.blockout.world.items.Weapon;
import org.blockout.world.state.IStateMachine;
import org.newdawn.slick.util.pathfinding.PathFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of a simple artificial intelligence (AI). It searches the
 * terrain for enemies and crates.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class SimpleAIPlayer extends AbstractAIPlayer {

	private static final Logger	logger;
	static {
		logger = LoggerFactory.getLogger( SimpleAIPlayer.class );
	}

	private ITarget				currentTarget;
	private final Random		rand;

	@Inject
	public SimpleAIPlayer(final IWorld world, final LocalGameState gameState, final Camera camera,
			final LocalPlayerMoveHandler playerController, final PathFinder pathFinder, final IStateMachine stateMachine) {

		super( world, gameState, camera, playerController, pathFinder, stateMachine );
		rand = new Random( System.currentTimeMillis() );
	}

	@Override
	public void doNextStep() {
		if ( getGameState().isGameInitialized() ) {

			if ( currentTarget == null || currentTarget.achieved() ) {
				currentTarget = findTarget();
				logger.info( "Nothing to do. Next target will be " + currentTarget );
			} else {
				ITarget possibleTarget = findTarget();
				if ( possibleTarget.getPriority() > currentTarget.getPriority()
						&& !(possibleTarget.equals( currentTarget )) ) {
					logger.info( "Discovered target with higher priority: " + possibleTarget );
					currentTarget = possibleTarget;
				}
			}

			handleInventory();

			logger.debug( "Current target: " + currentTarget + ", current position: "
					+ getWorld().findTile( getGameState().getPlayer() ) );
			currentTarget.approach();
		}
	}

	private void handleInventory() {
		Player player = getGameState().getPlayer();

		// Select best armor of inventory and discard old one
		Armor armor = AIUtils.takeBestArmor( player.getInventory() );
		if ( armor != null ) {
			if ( player.getEquipment().getArmor() == null
					|| player.getEquipment().getArmor().getProtection() < armor.getProtection() ) {
				logger.info( player + " found better armor. Replacing " + player.getEquipment().getArmor() + " with "
						+ armor );
				player.getEquipment().setArmor( armor );
			}
		}

		// Select best weapon of inventory and discard old one
		Weapon weapon = AIUtils.takeBestWeapon( player.getInventory() );
		if ( weapon != null ) {
			if ( player.getEquipment().getWeapon() == null
					|| player.getEquipment().getWeapon().getStrength() < weapon.getStrength() ) {
				logger.info( player + " found better weapon. Replacing " + player.getEquipment().getWeapon() + " with "
						+ weapon );
				player.getEquipment().setWeapon( weapon );
			}
		}

		// Select best shoes of inventory and discard old one
		Shoes shoes = AIUtils.takeBestShoes( player.getInventory() );
		if ( shoes != null ) {
			if ( player.getEquipment().getShoes() == null
					|| player.getEquipment().getShoes().getProtection() < shoes.getProtection() ) {
				logger.info( player + " found better shoes. Replacing " + player.getEquipment().getShoes() + " with "
						+ shoes );
				player.getEquipment().setShoes( shoes );
			}
		}

		// Select best shield of inventory and discard old one
		Shield shield = AIUtils.takeBestShield( player.getInventory() );
		if ( shield != null ) {
			if ( player.getEquipment().getShield() == null
					|| player.getEquipment().getShield().getProtection() < shield.getProtection() ) {
				logger.info( player + " found better shield. Replacing " + player.getEquipment().getShield() + " with "
						+ shield );
				player.getEquipment().setShield( shield );
			}
		}

		// Select best helm of inventory and discard old one
		Helm helm = AIUtils.takeBestHelm( player.getInventory() );
		if ( helm != null ) {
			if ( player.getEquipment().getHelm() == null
					|| player.getEquipment().getHelm().getProtection() < helm.getProtection() ) {
				logger.info( player + " found better helm. Replacing " + player.getEquipment().getHelm() + " with "
						+ helm );
				player.getEquipment().setHelm( helm );
			}
		}

		// Select best gloves of inventory and discard old one
		Gloves gloves = AIUtils.takeBestGloves( player.getInventory() );
		if ( gloves != null ) {
			if ( player.getEquipment().getGloves() == null
					|| player.getEquipment().getGloves().getProtection() < gloves.getProtection() ) {
				logger.info( player + " found better gloves. Replacing " + player.getEquipment().getGloves() + " with "
						+ gloves );
				player.getEquipment().setGloves( gloves );
			}
		}

		// Fill belt with elixirs
		while ( !player.getEquipment().isBeltFull() ) {
			Elixir elixir = player.getInventory().takeFirstItem( Elixir.class );
			if ( elixir == null ) {
				break;
			}
			player.getEquipment().putInBelt( elixir );
		}

		// Update player entity
		getWorld().setEnityPosition( player, getWorld().findTile( player ) );
	}

	private ITarget findTarget() {
		TileCoordinate currentPos = getWorld().findTile( getGameState().getPlayer() );
		//
		// 1. Slay nearby enemy - if found
		//
		Actor enemy = findNearbyEntity( currentPos, Actor.class );
		if ( enemy != null ) {
			return new SlayEnemyTarget( enemy, this );
		}
		//
		// 2. Open nearby crate - if found
		//
		Crate crate = findNearbyEntity( currentPos, Crate.class );
		if ( crate != null && crate.getItem() != null ) {
			return new OpenCrateTarget( crate, this );
		}

		crate = findNearestEntity( currentPos, Crate.class, getGameState().getPlayer() );
		enemy = findNearestEntity( currentPos, Actor.class, getGameState().getPlayer() );

		String reason = null;
		TileCoordinate walkToCoord = null;
		//
		// 3. Walk to closest crate - if any visible
		//
		if ( crate != null && crate.getItem() != null ) {
			TileCoordinate tile = getWorld().findTile( crate );
			logger.debug( "Found nearest crate " + crate + " at " + tile );
			walkToCoord = AIUtils.findWalkableTileNextTo( this, tile, currentPos );
			reason = "Walk to Crate";
		}
		//
		// 4. Walk to closest enemy - if any visible
		//
		if ( enemy != null ) {
			TileCoordinate tile = getWorld().findTile( enemy );
			logger.debug( "Found nearest enemy " + enemy + " at " + tile );
			TileCoordinate coordinate = AIUtils.findWalkableTileNextTo( this, tile, currentPos );
			if ( coordinate != null ) {
				if ( walkToCoord == null
						|| TileCoordinate.computeEuclidianDistance( coordinate, currentPos ) < TileCoordinate
								.computeEuclidianDistance( walkToCoord, currentPos ) ) {
					walkToCoord = coordinate;
					reason = "Walk to Enemy";
				}
			}
		}
		if ( walkToCoord != null ) {
			return new WalkToPositionTarget( walkToCoord, this, 2, reason );
		} else {
			// Nothing visible or reachable
		}

		//
		// 5. Walk to random position
		//
		Camera localCamera = getCamera().getReadOnly();
		int halfWidth = localCamera.getNumHorTiles() / 2;
		int halfHeight = localCamera.getNumVerTiles() / 2;
		return new WalkToPositionTarget( currentPos.plus( rand.nextInt( localCamera.getNumHorTiles() ) - halfWidth,
				rand.nextInt( localCamera.getNumVerTiles() ) - halfHeight ), this, 0, "Random Walking" );
	}

	private <T extends Entity> T findNearestEntity( final TileCoordinate center, final Class<T> clazz,
			final Entity except ) {
		// locks the camera state
		Camera localCamera = getCamera().getReadOnly();
		double distance = Double.MAX_VALUE;
		T result = null;

		int halfVerTile = localCamera.getNumVerTiles() / 2;
		int halfHorTiles = localCamera.getNumHorTiles() / 2;

		for ( int y = -halfVerTile; y < halfVerTile; y++ ) {
			for ( int x = -halfHorTiles; x < halfHorTiles; x++ ) {

				TileCoordinate currentTile = center.plus( x, y );

				T entity = getEntityOrNull( currentTile, clazz );
				if ( entity != null && !entity.equals( except ) ) {
					double dist = TileCoordinate.computeSquaredEuclidianDistance( center, currentTile );
					if ( dist < distance ) {
						distance = dist;
						result = entity;
					}
				}
			}
		}
		return result;
	}

	private <T extends Entity> T findNearbyEntity( final TileCoordinate center, final Class<T> clazz ) {
		T actor = getEntityOrNull( center.plus( 1, 1 ), clazz );
		if ( actor == null ) {
			actor = getEntityOrNull( center.plus( 1, 0 ), clazz );
		}
		if ( actor == null ) {
			actor = getEntityOrNull( center.plus( 1, -1 ), clazz );
		}
		if ( actor == null ) {
			actor = getEntityOrNull( center.plus( 0, -1 ), clazz );
		}
		if ( actor == null ) {
			actor = getEntityOrNull( center.plus( 0, 1 ), clazz );
		}
		if ( actor == null ) {
			actor = getEntityOrNull( center.plus( -1, 1 ), clazz );
		}
		if ( actor == null ) {
			actor = getEntityOrNull( center.plus( -1, 0 ), clazz );
		}
		if ( actor == null ) {
			actor = getEntityOrNull( center.plus( -1, -1 ), clazz );
		}
		return actor;
	}

	private <T extends Entity> T getEntityOrNull( final TileCoordinate coord, final Class<T> clazz ) {
		Tile tile = getWorld().getTile( coord );
		Entity entity = tile.getEntityOnTile();
		if ( entity == null || !clazz.isInstance( entity ) ) {
			return null;
		}
		return clazz.cast( entity );
	}
}
