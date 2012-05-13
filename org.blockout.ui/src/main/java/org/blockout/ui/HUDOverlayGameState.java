package org.blockout.ui;

/*     */
/*     */import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.slick2d.input.SlickInputSystem;
import de.lessvoid.nifty.slick2d.input.SlickSlickInputSystem;
import de.lessvoid.nifty.slick2d.render.SlickRenderDevice;
import de.lessvoid.nifty.slick2d.sound.SlickSoundDevice;
import de.lessvoid.nifty.slick2d.time.LWJGLTimeProvider;
import de.lessvoid.nifty.spi.time.TimeProvider;

/*     */
/*     */public abstract class HUDOverlayGameState extends BasicGameState
/*     */{
	/*     */private SlickInputSystem	inputSystem;
	/*     */private Nifty				niftyGUI;

	/*     */
	/*     */@Override
	public final void enter( final GameContainer container, final StateBasedGame game )
	/*     */throws SlickException
	/*     */{
		/* 34 */Input input = container.getInput();
		/* 35 */input.removeListener( inputSystem );
		/* 36 */input.addListener( inputSystem );
		/*     */
		/* 38 */enterState( container, game );
		/*     */}

	/*     */
	/*     */protected abstract void enterState( GameContainer paramGameContainer, StateBasedGame paramStateBasedGame )
	/*     */throws SlickException;

	/*     */
	/*     */public final Nifty getNifty()
	/*     */{
		/* 60 */return niftyGUI;
		/*     */}

	/*     */
	/*     */@Override
	public final void init( final GameContainer container, final StateBasedGame game )
	/*     */throws SlickException
	/*     */{
		/* 68 */initGameAndGUI( container, game );
		/*     */
		/* 70 */if ( niftyGUI == null ) {
			/* 71 */throw new IllegalStateException( "NiftyGUI was not initialized." );
			/*     */}
		/*     */
		/* 74 */container.getInput().removeListener( game );
		/*     */}

	/*     */
	/*     */protected abstract void initGameAndGUI( GameContainer paramGameContainer, StateBasedGame paramStateBasedGame )
	/*     */throws SlickException;

	/*     */
	/*     */protected final void initNifty( final GameContainer container, final StateBasedGame game,
			final SlickRenderDevice renderDevice, final SlickSoundDevice soundDevice,
			final SlickInputSystem inputSystem, final TimeProvider timeProvider )
	/*     */{
		/* 116 */if ( niftyGUI != null ) {
			/* 117 */throw new IllegalStateException(
					"The NiftyGUI was already initialized. Its illegal to do so twice." );
			/*     */}
		/*     */
		/* 120 */inputSystem.setInput( container.getInput() );
		/*     */
		/* 122 */niftyGUI = new Nifty( renderDevice, soundDevice, inputSystem, timeProvider );
		/*     */
		/* 124 */this.inputSystem = inputSystem;
		/*     */
		/* 126 */prepareNifty( niftyGUI, game );
		/*     */}

	/*     */
	/*     */protected final void initNifty( final GameContainer container, final StateBasedGame game,
			final SlickRenderDevice renderDevice, final SlickSoundDevice soundDevice, final SlickInputSystem inputSystem )
	/*     */{
		/* 152 */initNifty( container, game, renderDevice, soundDevice, inputSystem, new LWJGLTimeProvider() );
		/*     */}

	/*     */
	/*     */protected final void initNifty( final GameContainer container, final StateBasedGame game,
			final SlickInputSystem inputSystem )
	/*     */{
		/* 175 */initNifty( container, game, new SlickRenderDevice( container ), new SlickSoundDevice(), inputSystem );
		/*     */}

	/*     */
	/*     */protected final void initNifty( final GameContainer container, final StateBasedGame game )
	/*     */{
		/* 196 */initNifty( container, game, new SlickSlickInputSystem( this ) );
		/*     */}

	/*     */
	/*     */@Override
	public final void leave( final GameContainer container, final StateBasedGame game )
	/*     */throws SlickException
	/*     */{
		/* 204 */Input input = container.getInput();
		/* 205 */input.removeListener( inputSystem );
		/*     */
		/* 207 */leaveState( container, game );
		/*     */}

	/*     */
	/*     */protected abstract void leaveState( GameContainer paramGameContainer, StateBasedGame paramStateBasedGame )
	/*     */throws SlickException;

	/*     */
	/*     */protected abstract void prepareNifty( Nifty paramNifty, StateBasedGame paramStateBasedGame );

	/*     */
	/*     */@Override
	public final void render( final GameContainer container, final StateBasedGame game, final Graphics g )
	/*     */throws SlickException
	/*     */{
		/* 240 */renderGame( container, game, g );
		/*     */
		/* 242 */if ( niftyGUI != null ) {
			/* 243 */niftyGUI.render( false );

			renderHUDOverlay( container, game, g );
			/*     */
		}
	}

	/*     */
	/*     */protected abstract void renderGame( GameContainer paramGameContainer, StateBasedGame paramStateBasedGame,
			Graphics paramGraphics )
	/*     */throws SlickException;

	protected void renderHUDOverlay( final GameContainer paramGameContainer, final StateBasedGame paramStateBasedGame,
			final Graphics paramGraphics ) throws SlickException {

	}

	/*     */
	/*     */@Override
	public final void update( final GameContainer container, final StateBasedGame game, final int delta )
	/*     */throws SlickException
	/*     */{
		/* 266 */updateGame( container, game, delta );
		/*     */
		/* 268 */if ( niftyGUI != null ) {
			/* 269 */niftyGUI.update();
			/*     */
		}
	}

	/*     */
	/*     */protected abstract void updateGame( GameContainer paramGameContainer, StateBasedGame paramStateBasedGame,
			int paramInt )
	/*     */throws SlickException;
	/*     */
}

/*
 * Location:
 * C:\Users\azrael\.m2\repository\lessvoid\nifty-slick-renderer\1.3.1\nifty
 * -slick-renderer-1.3.1.jar Qualified Name:
 * de.lessvoid.nifty.slick2d.NiftyOverlayBasicGameState Java Class Version: 6
 * (50.0) JD-Core Version: 0.5.3
 */