package org.blockout.ui;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.engine.ISpriteManager;
import org.blockout.logic.FogOfWar;
import org.blockout.world.IWorld;
import org.blockout.world.LocalGameState;
import org.blockout.world.event.PlayerMoveEvent;
import org.blockout.world.state.IStateMachine;
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
public class InGameGameState extends NiftyOverlayBasicGameState implements ScreenController {

	private Nifty						nifty;

	// How long it takes to move the player to a new position. measured in
	// milliseconds
	private static final float			PLAYER_MOVE_TIME	= 500.0f;

	private float						centerX				= 0;
	private float						centerY				= 0;

	private final FogOfWarWorldRenderer	worldRenderer;
	private Element						exitPopup;

	private final InputHandler			inputHandler;

	private Label						lblPlayer;
	private Label						lblHealth;
	private Label						lblXP;
	private Label						lblLevel;

	private final LocalGameState		gameState;
	private final IStateMachine			stateMachine;
	protected Camera					camera;

	@Inject
	public InGameGameState(final ISpriteManager spriteManager, final IWorld world, final InputHandler inputHandler,
			final LocalGameState gameState, final IStateMachine stateMachine, final Camera camera, final FogOfWar fog) {
		this.inputHandler = inputHandler;
		this.gameState = gameState;
		this.stateMachine = stateMachine;
		this.camera = camera;
		worldRenderer = new FogOfWarWorldRenderer( spriteManager, world, camera, gameState, fog );
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

		updatePlayerPosition( container, deltaMillis );

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

	private void updatePlayerPosition( final GameContainer container, final int deltaMillis ) {
		float currentX = gameState.getCurrentPlayerX();
		float desiredX = gameState.getDesiredPlayerX();
		float desiredY = gameState.getDesiredPlayerY();
		float currentY = gameState.getCurrentPlayerY();

		// otherwise we would raise an event flood
		if ( currentX == desiredX && currentY == desiredY ) {

			// TODO: move this code in separate InputHandler
			int x = 0;
			int y = 0;
			if ( container.getInput().isKeyDown( Input.KEY_UP ) ) {
				y++;
			}
			if ( container.getInput().isKeyDown( Input.KEY_DOWN ) ) {
				y--;
			}
			if ( container.getInput().isKeyDown( Input.KEY_LEFT ) ) {
				x--;
			}
			if ( container.getInput().isKeyDown( Input.KEY_RIGHT ) ) {
				x++;
			}

			if ( x != 0 || y != 0 ) {
				stateMachine.pushEvent( new PlayerMoveEvent( (int) currentX, (int) currentY, (int) currentX + x,
						(int) currentY + y ) );
			}
		} else {

			float deltaX = desiredX - currentX;
			centerX += deltaX * (deltaMillis / PLAYER_MOVE_TIME);
			if ( (deltaX > 0 && centerX >= desiredX) || (deltaX < 0 && centerX <= desiredX) ) {
				centerX = desiredX;
				gameState.setCurrentPlayerX( gameState.getDesiredPlayerX() );
			}

			float deltaY = desiredY - currentY;
			centerY += deltaY * (deltaMillis / PLAYER_MOVE_TIME);
			if ( (deltaY > 0 && centerY >= desiredY) || (deltaY < 0 && centerY <= desiredY) ) {
				centerY = desiredY;
				gameState.setCurrentPlayerY( gameState.getDesiredPlayerY() );
			}

		}
		camera.setViewCenter( centerX + 0.5f, centerY + 0.5f );
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
		System.out.println( "Current Screen: " + nifty.getCurrentScreen().getScreenId() );

	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void leaveState( final GameContainer container, final StateBasedGame arg1 ) throws SlickException {
		// TODO Auto-generated method stub

		container.getInput().removeKeyListener( inputHandler );
		container.getInput().removeMouseListener( inputHandler );
	}
}
