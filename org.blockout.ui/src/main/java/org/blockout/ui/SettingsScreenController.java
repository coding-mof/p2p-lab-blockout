package org.blockout.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Named;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.screen.Screen;

@Named
public class SettingsScreenController implements StateBasedScreenController {

	private static final Logger		logger;
	static {
		logger = LoggerFactory.getLogger( SettingsScreenController.class );
	}
	private StateBasedGame			game;

	private Nifty					nifty;
	private DropDown<DisplayMode>	dropDown;

	private DisplayMode				selectedDisplayMode;

	@Override
	public void bind( final Nifty nifty, final Screen screen ) {
		this.nifty = nifty;
		dropDown = screen.findNiftyControl( "resolutions", DropDown.class );

		// get all DisplayModes from LWJGL and add their descriptions to the
		// DropDown
		fillResolutionDropDown( screen );

		// and make sure the current is selected too
		dropDown.selectItem( Display.getDisplayMode() );
	}

	@NiftyEventSubscriber(id = "resolutions")
	public void onResolution( final String id, final DropDownSelectionChangedEvent<DisplayMode> event ) {
		selectedDisplayMode = event.getSelection();

	}

	/**
	 * Get all LWJGL DisplayModes into the DropDown
	 * 
	 * @param screen
	 */
	private void fillResolutionDropDown( final Screen screen ) {
		try {
			// DisplayMode currentMode = Display.getDisplayMode();
			List<DisplayMode> sorted = new ArrayList<DisplayMode>();

			DisplayMode[] modes = Display.getAvailableDisplayModes();
			for ( DisplayMode mode : modes ) {
				// && mode.getFrequency() == currentMode.getFrequency()
				if ( mode.getBitsPerPixel() == 32 ) {
					sorted.add( mode );
				}
			}

			Collections.sort( sorted, new Comparator<DisplayMode>() {
				@Override
				public int compare( final DisplayMode o1, final DisplayMode o2 ) {
					int widthCompare = Integer.valueOf( o1.getWidth() ).compareTo( Integer.valueOf( o2.getWidth() ) );
					if ( widthCompare != 0 ) {
						return widthCompare;
					}
					int heightCompare = Integer.valueOf( o1.getHeight() ).compareTo( Integer.valueOf( o2.getHeight() ) );
					if ( heightCompare != 0 ) {
						return heightCompare;
					}
					return o1.toString().compareTo( o2.toString() );
				}
			} );

			for ( DisplayMode mode : sorted ) {
				dropDown.addItem( mode );
			}
		} catch ( Exception e ) {
			logger.error( "Failed to list display modes.", e );
		}
	}

	public void goBack() {
		nifty.gotoScreen( "start" );
	}

	public void apply() {
		try {
			// Display.setDisplayMode( selectedDisplayMode );

			AppGameContainer container = (AppGameContainer) game.getContainer();
			container.setDisplayMode( selectedDisplayMode.getWidth(), selectedDisplayMode.getHeight(), false );

			// GL11.glMatrixMode( GL11.GL_PROJECTION );
			// GL11.glLoadIdentity();
			// GL11.glOrtho( 0, selectedDisplayMode.getWidth(),
			// selectedDisplayMode.getHeight(), 0, -9999, 9999 );
			//
			// GL11.glMatrixMode( GL11.GL_MODELVIEW );

			nifty.resolutionChanged();
			nifty.getRenderEngine().displayResolutionChanged();
		}
		// catch ( LWJGLException e ) {
		// logger.error( "Failed to change display mode to " +
		// selectedDisplayMode, e );
		// }
		catch ( SlickException e ) {
			logger.error( "Failed to change display mode to " + selectedDisplayMode, e );
		} catch ( Exception e ) {
			logger.error( "", e );
		}
	}

	@Override
	public void onStartScreen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindToGameState( final StateBasedGame game, final GameState state ) {
		this.game = game;
		System.out.println( "Bound to: " + game );
	}
}
