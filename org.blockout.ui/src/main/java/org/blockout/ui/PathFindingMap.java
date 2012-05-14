package org.blockout.ui;

import javax.inject.Inject;

import org.blockout.logic.FogOfWar;
import org.blockout.world.IWorld;
import org.blockout.world.Tile;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

/**
 * Adapter class for slick's path finding API. The path finding is only
 * performed for tiles which are in the view frustum. Furthermore all unexplored
 * tiles are treated as traverseable. This avoids that the player can use the
 * path finding algorithm to discover paths to hidden areas. The actual path
 * processing gets then validated by the common collision detection.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class PathFindingMap implements TileBasedMap {

	protected Camera	camera;
	protected FogOfWar	fog;
	protected IWorld	world;

	@Inject
	public PathFindingMap(final Camera camera, final FogOfWar fog, final IWorld world) {
		this.camera = camera;
		this.fog = fog;
		this.world = world;
	}

	@Override
	public boolean blocked( final PathFindingContext ctx, int x, int y ) {
		x += camera.getStartTileX();
		y += camera.getStartTileY();
		if ( !fog.isExplored( x, y ) ) {
			return false;
		}
		Tile tile = world.getTile( x, y );
		if ( tile == null ) {
			return true;
		}
		return (tile.getEntityOnTile() != null) || (tile.getHeight() > Tile.GROUND_HEIGHT);
	}

	@Override
	public float getCost( final PathFindingContext ctx, final int x, final int y ) {
		if ( ctx.getSourceX() == x || ctx.getSourceY() == y ) {
			return 1;
		}
		return 1.41f;
	}

	@Override
	public int getHeightInTiles() {
		try {
			camera.lock();

			return camera.getNumVerTiles();
		} finally {
			camera.unlock();
		}
	}

	@Override
	public int getWidthInTiles() {
		try {
			camera.lock();
			return camera.getNumHorTiles();
		} finally {
			camera.unlock();
		}
	}

	@Override
	public void pathFinderVisited( final int x, final int y ) {
	}
}
