package org.blockout.ui;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.engine.ISpriteManager;
import org.blockout.world.IWorld;
import org.bushe.swing.event.EventTopicSubscriber;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.slick2d.NiftyOverlayBasicGameState;
import de.lessvoid.nifty.slick2d.input.PlainSlickInputSystem;

/**
 * The main game state which renders the game.
 * 
 * @author Marc-Christian Schulze
 * 
 */
@Named
public class InGameGameState extends NiftyOverlayBasicGameState implements ScreenController {

	private Nifty						nifty;

	private float						centerX	= 0;
	private float						centerY	= 0;

	private final FogOfWarWorldRenderer	worldRenderer;
	private Element						exitPopup;
	private final ISpriteManager		spriteManager;

	// private Image playerSprite;

	private final InputHandler			inputHandler;

	@Inject
	public InGameGameState(final ISpriteManager spriteManager, final IWorld world, final InputHandler inputHandler) {
		this.spriteManager = spriteManager;
		this.inputHandler = inputHandler;
		worldRenderer = new FogOfWarWorldRenderer( spriteManager, world, 32, 1024, 768 );
	}

	@Override
	protected void prepareNifty( final Nifty nifty, final StateBasedGame game ) {
		this.nifty = nifty;

		nifty.fromXml( "ingame-screen.xml", "start" );

		// playerSprite = spriteManager.getSprite( SpriteType.Player );
		// Image bgSprite = spriteManager.getSprite( SpriteType.stoneground );
		// playerSprite = Utils.exclude( bgSprite, playerSprite );
	}

	@Override
	public int getID() {
		return 2;
	}

	@Override
	protected void renderGame( final GameContainer container, final StateBasedGame game, final Graphics g )
			throws SlickException {

		worldRenderer.render( g/* , playerSprite */);

		// TODO: render player & movement
	}

	@Override
	protected void enterState( final GameContainer container, final StateBasedGame game ) throws SlickException {
		getNifty().gotoScreen( "start" );
	}

	@Override
	protected void initGameAndGUI( final GameContainer container, final StateBasedGame game ) throws SlickException {
		initNifty( container, game, new PlainSlickInputSystem() );
		getNifty().gotoScreen( "start" );

		container.getInput().addKeyListener( inputHandler );
		container.getInput().addMouseListener( inputHandler );
	}

	@Override
	protected void updateGame( final GameContainer container, final StateBasedGame game, final int paramInt )
			throws SlickException {

		// TODO: move this code in separate InputHandler
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
		worldRenderer.setViewCenter( centerX, centerY );

		if ( container.getInput().isKeyDown( Input.KEY_ESCAPE ) ) {
			exitPopup = nifty.createPopup( "popupMenu" );
			Button btnReturnToGame = exitPopup.findNiftyControl( "btnReturnToGame", Button.class );
			nifty.subscribe( nifty.getCurrentScreen(), btnReturnToGame.getId(), ButtonClickedEvent.class,
					new EventTopicSubscriber<ButtonClickedEvent>() {

						@Override
						public void onEvent( final String arg0, final ButtonClickedEvent arg1 ) {
							nifty.closePopup( exitPopup.getId() );
						}
					} );
			nifty.showPopup( nifty.getCurrentScreen(), exitPopup.getId(), null );
		}

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
		System.out.println( "Current Screen: " + nifty.getCurrentScreen().getScreenId() );

	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void leaveState( final GameContainer arg0, final StateBasedGame arg1 ) throws SlickException {
		// TODO Auto-generated method stub

	}
}
