package org.blockout;

import org.newdawn.slick.util.LogSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapter class for redirecting the log messages of Slick to SLF4J.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class SLF4JLogSystem implements LogSystem {

	private static final Logger logger;
	static {
		logger = LoggerFactory.getLogger(SLF4JLogSystem.class);
	}

	@Override
	public void error(String message, Throwable e) {
		logger.error(message, e);
	}

	@Override
	public void error(Throwable e) {
		logger.error("", e);
	}

	@Override
	public void error(String message) {
		logger.error(message);
	}

	@Override
	public void warn(String message) {
		logger.warn(message);
	}

	@Override
	public void info(String message) {
		logger.info(message);
	}

	@Override
	public void debug(String message) {
		logger.debug(message);
	}
}