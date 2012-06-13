package org.blockout.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AIThread {

	private static final Logger	logger;
	static {
		logger = LoggerFactory.getLogger( AIThread.class );
	}

	public static final long	DEFAULT_RESPONSE_TIME	= 200;
	private final Object		threadLock				= new Object();
	private Thread				thread;
	private volatile boolean	keepPlaying				= true;

	private final long			responseTime;
	private final AIPlayer		player;

	public AIThread(final AIPlayer player) {
		this( player, DEFAULT_RESPONSE_TIME );
	}

	public AIThread(final AIPlayer player, final long responseTime) {
		this.player = player;
		this.responseTime = responseTime;
	}

	public void start() {
		synchronized ( threadLock ) {
			if ( thread != null ) {
				return;
			}
			thread = new Thread( new Runnable() {

				@Override
				public void run() {
					play();
				}
			}, "AI Thread" );
			thread.start();
		}
	}

	public void stop() {
		synchronized ( threadLock ) {
			if ( thread != null ) {
				keepPlaying = false;
				logger.info( "Stopping AI Thread..." );
				try {
					thread.join();
				} catch ( InterruptedException e ) {
					logger.warn( "Joining on '" + thread.getName() + "' has been interrupted.", e );
				}
			}
		}
	}

	private void play() {
		logger.info( "AI Thread starts playing..." );
		long lastTime;
		while ( keepPlaying ) {
			lastTime = System.currentTimeMillis();
			player.doNextStep();
			long toSleep = responseTime - (System.currentTimeMillis() - lastTime);
			if ( toSleep > 0 ) {
				try {
					Thread.sleep( toSleep );
				} catch ( InterruptedException e ) {
					logger.warn( "Something interrupted the AI Thread. AIPlayer might play now faster than expected.",
							e );
				}
			}
		}
		logger.info( "AI Thread stopped playing." );
	}
}
