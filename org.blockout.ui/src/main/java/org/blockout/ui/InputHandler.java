package org.blockout.ui;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.world.LocalGameState;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;
import org.newdawn.slick.util.pathfinding.PathFinder;

@Named
public class InputHandler implements MouseListener, KeyListener {

	protected Camera			camera;
	protected PathFinder		pathFinder;
	protected LocalGameState	gameState;

	@Inject
	public InputHandler(final Camera camera, final PathFinder pathFinder, final LocalGameState gameState) {
		this.camera = camera;
		this.pathFinder = pathFinder;
		this.gameState = gameState;
	}

	@Override
	public void inputEnded() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputStarted() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAcceptingInput() {
		return true;
	}

	@Override
	public void setInput( final Input input ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed( final int arg0, final char arg1 ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased( final int arg0, final char arg1 ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked( final int button, final int x, final int y, final int clickCount ) {
	}

	@Override
	public void mouseDragged( final int oldx, final int oldy, final int newx, final int newy ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved( final int oldx, final int oldy, final int newx, final int newy ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed( final int button, final int x, final int y ) {

	}

	@Override
	public void mouseReleased( final int button, final int x, final int y ) {
		int tileX = camera.worldToTile( camera.screenToWorldX( x ) );
		int tileY = camera.worldToTile( camera.screenToWorldY( y ) );
		int centerX = camera.worldToTile( camera.getCenterX() );
		int centerY = camera.worldToTile( camera.getCenterY() );

		int fromX = centerX - camera.getStartTileX();
		int fromY = centerY - camera.getStartTileY();
		int toX = tileX - camera.getStartTileX();
		int toY = tileY - camera.getStartTileY();

		Path path = pathFinder.findPath( gameState.getPlayer(), fromX, fromY, toX, toY );

		if ( path == null ) {
			System.out.println( "No path available." );
			return;
		}
		System.out.println( "Path: " + path );
		for ( int i = 0; i < path.getLength(); i++ ) {
			Step step = path.getStep( i );
			System.out.println( "  Tile: (" + (step.getX() + camera.getStartTileX()) + ","
					+ (step.getY() + camera.getStartTileY()) + ")" );
		}
	}

	@Override
	public void mouseWheelMoved( final int change ) {
		// TODO Auto-generated method stub

	}

}
