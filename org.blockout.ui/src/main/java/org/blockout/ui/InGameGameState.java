package org.blockout.ui;

import javax.inject.Named;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.slick2d.NiftyOverlayBasicGameState;
import de.lessvoid.nifty.slick2d.input.PlainSlickInputSystem;

@Named
public class InGameGameState extends NiftyOverlayBasicGameState {

	private Image			tile;

	private float			centerX	= 0;
	private float			centerY	= 0;

	private WorldRenderer	renderer;

	@Override
	protected void prepareNifty( final Nifty nifty, final StateBasedGame game ) {
		nifty.fromXml( "ingame-screen.xml", "start" );
		// nifty.setDebugOptionPanelColors( true );
		try {
			tile = new Image( "tile_white.png" );
		} catch ( SlickException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		renderer = new WorldRenderer( 32, 1024, 768 );
	}

	@Override
	public int getID() {
		return 2;
	}

	@Override
	protected void renderGame( final GameContainer container, final StateBasedGame game, final Graphics g )
			throws SlickException {

		// TODO: game rendering
		renderer.render( g, tile );

	}

	@Override
	protected void enterState( final GameContainer container, final StateBasedGame game ) throws SlickException {
		getNifty().gotoScreen( "start" );
	}

	@Override
	protected void initGameAndGUI( final GameContainer container, final StateBasedGame game ) throws SlickException {
		initNifty( container, game, new PlainSlickInputSystem() );
		getNifty().gotoScreen( "start" );
	}

	@Override
	protected void leaveState( final GameContainer container, final StateBasedGame game ) throws SlickException {
	}

	@Override
	protected void updateGame( final GameContainer container, final StateBasedGame game, final int paramInt )
			throws SlickException {

		System.out.println( "Delta: " + paramInt );
		System.out.println( "t:" + (paramInt / 1000f) );

		if ( container.getInput().isKeyDown( Input.KEY_W ) ) {
			centerY += 2.0 * (paramInt / 1000f);
		}

		if ( container.getInput().isKeyDown( Input.KEY_S ) ) {
			centerY -= 2.0 * (paramInt / 1000f);
		}

		if ( container.getInput().isKeyDown( Input.KEY_A ) ) {
			centerX -= 2.0 * (paramInt / 1000f);
		}

		if ( container.getInput().isKeyDown( Input.KEY_D ) ) {
			centerX += 2.0 * (paramInt / 1000f);
		}

		renderer.setViewCenter( centerX, centerY );
	}

}
