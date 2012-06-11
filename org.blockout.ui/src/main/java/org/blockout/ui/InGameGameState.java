package org.blockout.ui;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.engine.sfx.AudioType;
import org.blockout.engine.sfx.IAudioManager;
import org.blockout.world.LocalGameState;
import org.blockout.world.items.Elixir;
import org.blockout.world.items.Elixir.Type;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * The main game state which renders the game.
 * 
 * @author Marc-Christian Schulze
 * 
 * @see GameStates#InGame
 */
@Named
public final class InGameGameState extends HUDOverlayGameState implements ScreenController {

	private static final Logger				logger;
	static {
		logger = LoggerFactory.getLogger( InGameGameState.class );
	}

	private Label							lblPlayer;
	private Label							lblHealth;
	private Label							lblXP;
	private Label							lblLevel;

	private final IWorldRenderer			worldRenderer;
	private final LocalGameState			gameState;
	private final InputHandler				inputHandler;
	private final LocalPlayerMoveHandler	playerController;

	private IHealthRenderer					healthRenderer;
	private final IAudioManager				audioManager;

	protected AutowireCapableBeanFactory	beanFactory;

	@Inject
	public InGameGameState(final IWorldRenderer worldRenderer, final InputHandler inputHandler,
			final LocalGameState gameState, final Camera camera, final LocalPlayerMoveHandler playerController,
			final IAudioManager audioManager, final AutowireCapableBeanFactory beanFactory) {
		super( inputHandler );
		this.inputHandler = inputHandler;
		this.gameState = gameState;
		this.worldRenderer = worldRenderer;
		this.playerController = playerController;
		this.audioManager = audioManager;

		healthRenderer = new ShaderBasedHealthRenderer( camera, gameState );

		this.beanFactory = beanFactory;
	}

	@Override
	protected void prepareNifty( final Nifty nifty, final StateBasedGame game ) {
		nifty.fromXml( "ingame-screen.xml", "start" );
		// Since Nifty requires to create it's own instance of the Controller -
		// we need to autowire it here
		Controller ctrl = nifty.getCurrentScreen().findElementByName( "inventory" ).getControl( Controller.class );
		beanFactory.autowireBean( ctrl );

		inputHandler.setNifty( nifty );
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
	protected void renderHUDOverlay( final GameContainer paramGameContainer, final StateBasedGame paramStateBasedGame,
			final Graphics paramGraphics ) throws SlickException {

		try {
			healthRenderer.render();
		} catch ( UnsupportedOperationException e ) {
			logger.warn( "Failed to render health. Falling back...", e );
			healthRenderer = new PrimitiveHealthRenderer();
		}
	}

	@Override
	protected void enterState( final GameContainer container, final StateBasedGame game ) throws SlickException {
		getNifty().gotoScreen( "start" );

		audioManager.getMusic( AudioType.music_irish_meadow ).loop();

		inputHandler.setGameContainer( container );

		// TODO: this is just for debugging...
		// TODO: implement logic to collect items...
		System.out.println( "-------------- FILLING INEVNTORY ------------------" );
		gameState.getPlayer().getInventory().setItem( 0, 0, new Elixir( Type.Health, 50 ) );
		gameState.getPlayer().getInventory().setItem( 1, 1, new Elixir( Type.Health, 10 ) );
		gameState.getPlayer().getInventory().setItem( 2, 2, new Elixir( Type.Health, 30 ) );
	}

	@Override
	protected void updateGame( final GameContainer container, final StateBasedGame game, final int deltaMillis )
			throws SlickException {

		healthRenderer.update( deltaMillis );

		updateHUD();

		playerController.update( container, deltaMillis );

	}

	private void updateHUD() {
		Screen screen = getNifty().getCurrentScreen();

		lblPlayer = screen.findNiftyControl( "lblPlayer", Label.class );
		lblHealth = screen.findNiftyControl( "lblHealth", Label.class );
		lblXP = screen.findNiftyControl( "lblXP", Label.class );
		lblLevel = screen.findNiftyControl( "lblLevel", Label.class );

		lblPlayer.setText( gameState.getPlayer().getName() );
		lblHealth.setText( gameState.getPlayer().getCurrentHealth() + " / " + gameState.getPlayer().getMaxHealth() );
		lblXP.setText( "" + gameState.getPlayer().getExperience() + " / "
				+ gameState.getPlayer().getRequiredExperience() );
		lblLevel.setText( "" + gameState.getPlayer().getLevel() );
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
	}
}
