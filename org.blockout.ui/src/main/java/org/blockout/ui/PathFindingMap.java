package org.blockout.ui;

import javax.inject.Inject;

import org.blockout.logic.FogOfWar;
import org.blockout.world.IWorld;
import org.blockout.world.Tile;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

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
		return camera.getNumVerTiles();
	}

	@Override
	public int getWidthInTiles() {
		return camera.getNumHorTiles();
	}

	@Override
	public void pathFinderVisited( final int x, final int y ) {
		// TODO Auto-generated method stub

	}
}
