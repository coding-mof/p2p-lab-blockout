package org.blockout.ui;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.engine.ISpriteManager;
import org.blockout.engine.SpriteType;
import org.blockout.engine.Utils;
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

	private Nifty						nifty;

	private float						centerX	= 0;
	private float						centerY	= 0;

	private final FogOfWarWorldRenderer	worldRenderer;
	private Element						exitPopup;
	private final ISpriteManager		spriteManager;

	private Image						playerSprite;

	@Inject
	public InGameGameState(final ISpriteManager spriteManager, final IWorld world) {
		this.spriteManager = spriteManager;
		worldRenderer = new FogOfWarWorldRenderer( spriteManager, world, 32, 1024, 768 );
	}

	@Override
	protected void prepareNifty( final Nifty nifty, final StateBasedGame game ) {
		this.nifty = nifty;

		nifty.fromXml( "ingame-screen.xml", "start" );

		playerSprite = spriteManager.getSprite( SpriteType.Player );
		Image bgSprite = spriteManager.getSprite( SpriteType.StoneGround );
		playerSprite = Utils.exclude( bgSprite, playerSprite );
	}

	@Override
	public int getID() {
		return 2;
	}

	@Override
	protected void renderGame( final GameContainer container, final StateBasedGame game, final Graphics g )
			throws SlickException {

		// TODO: game rendering
		worldRenderer.render( g, playerSprite );

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

		if ( container.getInput().isKeyDown( Input.KEY_UP ) ) {
			centerY += 2.0 * (paramInt / 1000f);
		}

		if ( container.getInput().isKeyDown( Input.KEY_DOWN ) ) {
			centerY -= 2.0 * (paramInt / 1000f);
		}

		if ( container.getInput().isKeyDown( Input.KEY_LEFT ) ) {
			centerX -= 2.0 * (paramInt / 1000f);
		}

		if ( container.getInput().isKeyDown( Input.KEY_RIGHT ) ) {
			centerX += 2.0 * (paramInt / 1000f);
		}

		if ( container.getInput().isKeyDown( Input.KEY_ESCAPE ) ) {
			exitPopup = nifty.createPopup( "popupMenu" );
			nifty.showPopup( nifty.getCurrentScreen(), exitPopup.getId(), null );
		}

		worldRenderer.setViewCenter( centerX, centerY );
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
