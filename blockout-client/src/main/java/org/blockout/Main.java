package org.blockout;

import java.text.ParseException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import de.lessvoid.nifty.slick2d.loaders.SlickAddLoaderLocation;
import de.lessvoid.nifty.slick2d.loaders.SlickRenderImageLoaders;

/**
 * Main class for bootstrapping Spring's application context and thereby the
 * whole game.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class Main {

	public static void main( final String[] args ) throws SlickException, ParseException {

		Arguments arguments = parseArgumentsOrExit( args );

		Logger logger = LoggerFactory.getLogger( Main.class );
		try {

			ClassPathXmlApplicationContext context;
			if ( arguments.isHeadless() ) {
				logger.info( "Starting in headless mode..." );
				context = new ClassPathXmlApplicationContext( "headless-context.xml" );
			} else {
				logger.info( "Starting in UI based mode..." );
				java.util.logging.Logger.getAnonymousLogger().getParent().setLevel( java.util.logging.Level.SEVERE );
				java.util.logging.Logger.getLogger( "de.lessvoid.nifty.*" ).setLevel( java.util.logging.Level.SEVERE );

				context = new ClassPathXmlApplicationContext( "application.xml" );

				Log.setLogSystem( new SLF4JLogSystem() );
				AppGameContainer app = context.getBean( AppGameContainer.class );

				SlickRenderImageLoaders.getInstance().loadDefaultLoaders( SlickAddLoaderLocation.last );

				app.setDisplayMode( 1024, 768, false );
				app.setAlwaysRender( true );
				app.start();
			}

		} catch ( Exception e ) {
			logger.error( "Program terminated due to exception.", e );
		}
	}

	private static Arguments parseArgumentsOrExit( final String[] args ) {
		Arguments arguments = new Arguments();
		JCommander commander = new JCommander( arguments );
		try {
			commander.parse( args );
			if ( arguments.isShowHelp() ) {
				commander.usage();
				System.exit( 0 );
			}
		} catch ( ParameterException e ) {
			commander.usage();
			System.exit( -1 );
		}
		return arguments;
	}

}
