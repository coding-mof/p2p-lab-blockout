package org.blockout;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Game;
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
	public static void main(String[] args) throws SlickException {
		ClassPathXmlApplicationContext context;
		context = new ClassPathXmlApplicationContext("application.xml");

		Logger logger = LoggerFactory.getLogger(Main.class);

		Game game = context.getBean(Game.class);

		logger.info("Starting game at 800x600 (windowed)");

		Log.setLogSystem(new SLF4JLogSystem());
		AppGameContainer app = new AppGameContainer(game);

		app.setDisplayMode(800, 600, false);
		app.start();
	}

}
