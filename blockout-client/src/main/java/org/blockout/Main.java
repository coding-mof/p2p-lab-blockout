package org.blockout;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	/**
	 * @param args
	 * @throws SlickException
	 */
	public static void main( final String[] args ) throws SlickException {

		java.util.logging.Logger.getAnonymousLogger().getParent().setLevel( java.util.logging.Level.SEVERE );
		java.util.logging.Logger.getLogger( "de.lessvoid.nifty.*" ).setLevel( java.util.logging.Level.SEVERE );

		ClassPathXmlApplicationContext context;
		context = new ClassPathXmlApplicationContext( "application.xml" );

		Logger logger = LoggerFactory.getLogger( Main.class );

		Log.setLogSystem( new SLF4JLogSystem() );
		AppGameContainer app = context.getBean( AppGameContainer.class );

		app.setDisplayMode( 1024, 768, false );
		app.start();
	}

}
