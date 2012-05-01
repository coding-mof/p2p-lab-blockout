package org.blockout.ui;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.engine.ISpriteManager;
import org.blockout.world.IWorld;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.slick2d.NiftyOverlayBasicGameState;
import de.lessvoid.nifty.slick2d.input.PlainSlickInputSystem;

@Named
public class InGameGameState extends NiftyOverlayBasicGameState implements ScreenController {

	private Nifty				nifty;
	private Image				tile;

	private float				centerX	= 0;
	private float				centerY	= 0;

	private final WorldRenderer	renderer;
	private Element				exitPopup;

	@Inject
	public InGameGameState(final ISpriteManager spriteManager, final IWorld world) {
		renderer = new WorldRenderer( spriteManager, world, 32, 1024, 768 );
	}

	@Override
	protected void prepareNifty( final Nifty nifty, final StateBasedGame game ) {
		this.nifty = nifty;

		nifty.fromXml( "ingame-screen.xml", "start" );
		// nifty.setDebugOptionPanelColors( true );
		try {
			tile = new Image( "tile_white.png" );
		} catch ( SlickException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public int getID() {
		return 2;
	}

	@Override
	protected void renderGame( final GameContainer container, final StateBasedGame game, final Graphics g )
			throws SlickException {

		// TODO: game rendering
		renderer.render( g );

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

		if ( container.getInput().isKeyDown( Input.KEY_ESCAPE ) ) {
			exitPopup = nifty.createPopup( "popupMenu" );
			nifty.showPopup( nifty.getCurrentScreen(), exitPopup.getId(), null );
		}

		renderer.setViewCenter( centerX, centerY );
	}

	public void closePopup() {
		nifty.closePopup( exitPopup.getId() );
	}

	@Override
	public void bind( final Nifty paramNifty, final Screen paramScreen ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartScreen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub

	}
}
