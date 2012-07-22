package org.blockout.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.blockout.common.NetworkLogMessage;

import com.google.common.base.Preconditions;

public class NetworkLogPlayer implements Runnable {

	public static interface IMessageProcessor {
		public void process( NetworkLogMessage message );
	}

	Lock					playLock	= new ReentrantLock();
	AtomicBoolean			play		= new AtomicBoolean();
	AtomicInteger			delay		= new AtomicInteger();
	AtomicInteger			index		= new AtomicInteger();
	IMessageProcessor		processor;
	List<NetworkLogMessage>	messages;

	public NetworkLogPlayer(final List<NetworkLogMessage> messages, final IMessageProcessor processor) {
		Preconditions.checkNotNull( messages );
		Preconditions.checkNotNull( processor );

		this.messages = new LinkedList<NetworkLogMessage>( messages );
		this.processor = processor;
	}

	@Override
	public void run() {
		playLock.lock();
		play.set( true );

		while ( index.get() < messages.size() ) {
			if ( !play.get() ) {
				break;
			}

			NetworkLogMessage msg = messages.get( index.get() );
			processor.process( msg );
			index.incrementAndGet();

			long start = System.currentTimeMillis();
			long toWait = delay.get();
			while ( toWait > 0 ) {
				try {
					Thread.sleep( toWait );
				} catch ( InterruptedException e1 ) {
					// ignore
				}
				toWait = start + delay.get() - System.currentTimeMillis();
			}
		}

		play.set( false );
		playLock.unlock();
	}

	public void reset() {
		stop();
		index.set( 0 );
	}

	public void play() {
		playLock.lock();
		new Thread( this ).start();
		playLock.unlock();
	}

	public void stop() {
		play.set( false );
	}

	public void setDelay( final int delay ) {
		Preconditions.checkArgument( 0 <= delay );
		this.delay.set( delay );
	}
}
