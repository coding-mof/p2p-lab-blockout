package org.blockout.ai;

import org.blockout.common.TileCoordinate;
import org.blockout.engine.Camera;
import org.blockout.world.entity.Inventory;
import org.blockout.world.items.Armor;
import org.blockout.world.items.Gloves;
import org.blockout.world.items.Helm;
import org.blockout.world.items.Item;
import org.blockout.world.items.Shield;
import org.blockout.world.items.Shoes;
import org.blockout.world.items.Weapon;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class AIUtils {

	private static final Logger	logger;
	static {
		logger = LoggerFactory.getLogger( AIUtils.class );
	}

	/**
	 * Move the AI to a tile position
	 * 
	 * @param context
	 *            Current {@link AIContext}
	 * @param coord
	 *            Position where the AI should be moved
	 * 
	 * @throws NullPointerException
	 *             If context or coord are null
	 */
	public static boolean gotoTile( final AIContext context, final TileCoordinate coord ) {
		Preconditions.checkNotNull( context );
		Preconditions.checkNotNull( coord );

		Path worldPath = findPathTo( context, coord );
		if ( worldPath == null ) {
			return false;
		}

		context.getPlayerController().setPath( context.getStateMachine(), worldPath );
		return true;
	}

	public static TileCoordinate getCurrentDestination( final AIContext context ) {
		return context.getPlayerController().getDestination();
	}

	public static Armor takeBestArmor( final Inventory inventory ) {
		Armor current = null;
		int fromX = 0, fromY = 0;
		for ( int x = 0; x < inventory.getWidth(); x++ ) {
			for ( int y = 0; y < inventory.getHeight(); y++ ) {
				Item item = inventory.getItem( x, y );
				if ( item instanceof Armor ) {
					if ( current == null || ((Armor) item).getProtection() > current.getProtection() ) {
						current = (Armor) item;
						fromX = x;
						fromY = y;
					}
				}
			}
		}
		if ( current != null ) {
			inventory.removeItem( fromX, fromY );
		}
		return current;
	}

	public static Weapon takeBestWeapon( final Inventory inventory ) {
		Weapon current = null;
		int fromX = 0, fromY = 0;
		for ( int x = 0; x < inventory.getWidth(); x++ ) {
			for ( int y = 0; y < inventory.getHeight(); y++ ) {
				Item item = inventory.getItem( x, y );
				if ( item instanceof Weapon ) {
					if ( current == null || ((Weapon) item).getStrength() > current.getStrength() ) {
						current = (Weapon) item;
						fromX = x;
						fromY = y;
					}
				}
			}
		}
		if ( current != null ) {
			inventory.removeItem( fromX, fromY );
		}
		return current;
	}

	public static Shoes takeBestShoes( final Inventory inventory ) {
		Shoes current = null;
		int fromX = 0, fromY = 0;
		for ( int x = 0; x < inventory.getWidth(); x++ ) {
			for ( int y = 0; y < inventory.getHeight(); y++ ) {
				Item item = inventory.getItem( x, y );
				if ( item instanceof Shoes ) {
					if ( current == null || ((Shoes) item).getProtection() > current.getProtection() ) {
						current = (Shoes) item;
						fromX = x;
						fromY = y;
					}
				}
			}
		}
		if ( current != null ) {
			inventory.removeItem( fromX, fromY );
		}
		return current;
	}

	public static Helm takeBestHelm( final Inventory inventory ) {
		Helm current = null;
		int fromX = 0, fromY = 0;
		for ( int x = 0; x < inventory.getWidth(); x++ ) {
			for ( int y = 0; y < inventory.getHeight(); y++ ) {
				Item item = inventory.getItem( x, y );
				if ( item instanceof Helm ) {
					if ( current == null || ((Helm) item).getProtection() > current.getProtection() ) {
						current = (Helm) item;
						fromX = x;
						fromY = y;
					}
				}
			}
		}
		if ( current != null ) {
			inventory.removeItem( fromX, fromY );
		}
		return current;
	}

	public static Shield takeBestShield( final Inventory inventory ) {
		Shield current = null;
		int fromX = 0, fromY = 0;
		for ( int x = 0; x < inventory.getWidth(); x++ ) {
			for ( int y = 0; y < inventory.getHeight(); y++ ) {
				Item item = inventory.getItem( x, y );
				if ( item instanceof Shield ) {
					if ( current == null || ((Shield) item).getProtection() > current.getProtection() ) {
						current = (Shield) item;
						fromX = x;
						fromY = y;
					}
				}
			}
		}
		if ( current != null ) {
			inventory.removeItem( fromX, fromY );
		}
		return current;
	}

	public static Gloves takeBestGloves( final Inventory inventory ) {
		Gloves current = null;
		int fromX = 0, fromY = 0;
		for ( int x = 0; x < inventory.getWidth(); x++ ) {
			for ( int y = 0; y < inventory.getHeight(); y++ ) {
				Item item = inventory.getItem( x, y );
				if ( item instanceof Gloves ) {
					if ( current == null || ((Gloves) item).getProtection() > current.getProtection() ) {
						current = (Gloves) item;
						fromX = x;
						fromY = y;
					}
				}
			}
		}
		if ( current != null ) {
			inventory.removeItem( fromX, fromY );
		}
		return current;
	}

	private static Path findPathTo( final AIContext context, final TileCoordinate coord ) {
		Camera localCamera = context.getCamera().getReadOnly();

		float cameraCenterX = localCamera.getCenterX();
		float cameraCenterY = localCamera.getCenterY();

		int tileX = coord.getX();
		int tileY = coord.getY();
		int centerX = Camera.worldToTile( cameraCenterX );
		int centerY = Camera.worldToTile( cameraCenterY );

		// Handle Movements
		int fromX = centerX - localCamera.getStartTileX();
		int fromY = centerY - localCamera.getStartTileY();
		int toX = tileX - localCamera.getStartTileX();
		int toY = tileY - localCamera.getStartTileY();

		Path path = context.getPathfinder().findPath( context.getGameState().getPlayer(), fromX, fromY, toX, toY );
		if ( path == null || path.getLength() == 0 ) {
			return null;
		}
		Path worldPath = new Path();
		if ( path != null ) {
			for ( int i = 0; i < path.getLength(); i++ ) {
				Step step = path.getStep( i );
				worldPath.appendStep( step.getX() + localCamera.getStartTileX(),
						step.getY() + localCamera.getStartTileY() );
			}
		}
		return worldPath;
	}

	public static TileCoordinate findWalkableTileNextTo( final AIContext context, final TileCoordinate coord,
			final TileCoordinate closestTo ) {
		Path path;
		TileCoordinate neighbourTile;
		TileCoordinate result = null;

		Camera camera = context.getCamera().getReadOnly();

		neighbourTile = coord.plus( 1, 1 );
		if ( camera.isInFrustum( Camera.worldToTile( neighbourTile ) ) ) {
			path = findPathTo( context, neighbourTile );
			if ( path != null ) {
				result = neighbourTile;
			}
		}

		neighbourTile = coord.plus( 1, 0 );
		if ( camera.isInFrustum( Camera.worldToTile( neighbourTile ) ) ) {
			path = findPathTo( context, neighbourTile );
			if ( path != null ) {
				if ( result == null
						|| TileCoordinate.computeEuclidianDistance( neighbourTile, closestTo ) < TileCoordinate
								.computeEuclidianDistance( result, closestTo ) ) {
					result = neighbourTile;
				}
			}
		}

		neighbourTile = coord.plus( 1, -1 );
		if ( camera.isInFrustum( Camera.worldToTile( neighbourTile ) ) ) {
			path = findPathTo( context, neighbourTile );
			if ( path != null ) {
				if ( result == null
						|| TileCoordinate.computeEuclidianDistance( neighbourTile, closestTo ) < TileCoordinate
								.computeEuclidianDistance( result, closestTo ) ) {
					result = neighbourTile;
				}
			}
		}

		neighbourTile = coord.plus( -1, 1 );
		if ( camera.isInFrustum( Camera.worldToTile( neighbourTile ) ) ) {
			path = findPathTo( context, neighbourTile );
			if ( path != null ) {
				if ( result == null
						|| TileCoordinate.computeEuclidianDistance( neighbourTile, closestTo ) < TileCoordinate
								.computeEuclidianDistance( result, closestTo ) ) {
					result = neighbourTile;
				}
			}
		}

		neighbourTile = coord.plus( -1, 0 );
		if ( camera.isInFrustum( Camera.worldToTile( neighbourTile ) ) ) {
			path = findPathTo( context, neighbourTile );
			if ( path != null ) {
				if ( result == null
						|| TileCoordinate.computeEuclidianDistance( neighbourTile, closestTo ) < TileCoordinate
								.computeEuclidianDistance( result, closestTo ) ) {
					result = neighbourTile;
				}
			}
		}

		neighbourTile = coord.plus( -1, -1 );

		if ( camera.isInFrustum( Camera.worldToTile( neighbourTile ) ) ) {
			path = findPathTo( context, neighbourTile );
			if ( path != null ) {
				if ( result == null
						|| TileCoordinate.computeEuclidianDistance( neighbourTile, closestTo ) < TileCoordinate
								.computeEuclidianDistance( result, closestTo ) ) {
					result = neighbourTile;
				}
			}
		}

		neighbourTile = coord.plus( 0, 1 );
		if ( camera.isInFrustum( Camera.worldToTile( neighbourTile ) ) ) {
			path = findPathTo( context, neighbourTile );
			if ( path != null ) {
				if ( result == null
						|| TileCoordinate.computeEuclidianDistance( neighbourTile, closestTo ) < TileCoordinate
								.computeEuclidianDistance( result, closestTo ) ) {
					result = neighbourTile;
				}
			}
		}

		neighbourTile = coord.plus( 0, -1 );
		if ( camera.isInFrustum( Camera.worldToTile( neighbourTile ) ) ) {
			path = findPathTo( context, neighbourTile );
			if ( path != null ) {
				if ( result == null
						|| TileCoordinate.computeEuclidianDistance( neighbourTile, closestTo ) < TileCoordinate
								.computeEuclidianDistance( result, closestTo ) ) {
					result = neighbourTile;
				}
			}
		}

		return result;
	}
}
