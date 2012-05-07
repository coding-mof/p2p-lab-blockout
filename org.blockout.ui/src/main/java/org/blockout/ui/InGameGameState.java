package org.blockout.ui;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.world.LocalGameState;
import org.bushe.swing.event.EventTopicSubscriber;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.Label;
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
public final class InGameGameState extends NiftyOverlayBasicGameState implements ScreenController {

	private Nifty						nifty;

	private Element						exitPopup;

	private Label						lblPlayer;
	private Label						lblHealth;
	private Label						lblXP;
	private Label						lblLevel;

	protected final IWorldRenderer		worldRenderer;
	protected final LocalGameState		gameState;
	protected final InputHandler		inputHandler;
	protected final Camera				camera;
	protected final PlayerController	playerController;

	@Inject
	public InGameGameState(final IWorldRenderer worldRenderer, final InputHandler inputHandler,
			final LocalGameState gameState, final Camera camera, final PlayerController playerController) {
		this.inputHandler = inputHandler;
		this.gameState = gameState;
		this.camera = camera;
		this.worldRenderer = worldRenderer;
		this.playerController = playerController;
	}

	@Override
	protected void prepareNifty( final Nifty nifty, final StateBasedGame game ) {
		this.nifty = nifty;

		nifty.fromXml( "ingame-screen.xml", "start" );
	}

	@Override
	public int getID() {
		return GameStates.InGame.ordinal();
	}

	@Override
	protected void renderGame( final GameContainer container, final StateBasedGame game, final Graphics g )
			throws SlickException {

		worldRenderer.render( g );
	}

	@Override
	protected void enterState( final GameContainer container, final StateBasedGame game ) throws SlickException {
		getNifty().gotoScreen( "start" );

		container.getInput().addKeyListener( inputHandler );
		container.getInput().addMouseListener( inputHandler );
	}

	@Override
	protected void initGameAndGUI( final GameContainer container, final StateBasedGame game ) throws SlickException {
		initNifty( container, game, new PlainSlickInputSystem() );
		getNifty().gotoScreen( "start" );
	}

	@Override
	protected void updateGame( final GameContainer container, final StateBasedGame game, final int deltaMillis )
			throws SlickException {

		updateHUD();

		playerController.update( container, deltaMillis );

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

	private void updateHUD() {
		Screen screen = getNifty().getCurrentScreen();

		lblPlayer = screen.findNiftyControl( "lblPlayer", Label.class );
		lblHealth = screen.findNiftyControl( "lblHealth", Label.class );
		lblXP = screen.findNiftyControl( "lblXP", Label.class );
		lblLevel = screen.findNiftyControl( "lblLevel", Label.class );

		lblPlayer.setText( gameState.getPlayer().getName() );
		lblHealth.setText( gameState.getPlayer().getCurrentHealth() + " / " + gameState.getPlayer().getMaxHealth() );
		lblXP.setText( "" + gameState.getPlayer().getExperience() );
		lblLevel.setText( "" + gameState.getPlayer().getLevel() );
	}

	public void closePopup() {
		nifty.closePopup( exitPopup.getId() );
	}

	@Override
	public void bind( final Nifty paramNifty, final Screen screen ) {
	}

	@Override
	public void onStartScreen() {
	}

	@Override
	public void onEndScreen() {
	}

	@Override
	protected void leaveState( final GameContainer container, final StateBasedGame arg1 ) throws SlickException {
		container.getInput().removeKeyListener( inputHandler );
		container.getInput().removeMouseListener( inputHandler );
	}
}
