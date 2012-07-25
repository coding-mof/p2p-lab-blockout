package org.blockout.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.blockout.common.NetworkLogMessage;

import com.google.common.base.Preconditions;

/**
 * This NetworkLogPlayer reads the log entry by entry and calls a process with
 * these entries with a little delay between them.
 * 
 * @author Florian Müller
 */
public class NetworkLogPlayer implements Runnable {
    public static interface IMessageProcessor {
        public void process( NetworkLogMessage message );

        public void finished();
    }

    private Lock                    playLock = new ReentrantLock();
    private AtomicBoolean           play     = new AtomicBoolean();
    private AtomicInteger           delay    = new AtomicInteger();
    private AtomicInteger           index    = new AtomicInteger();
    private IMessageProcessor       processor;
    private List<NetworkLogMessage> messages;
    private int                     numLogMessages;

    /**
     * Create a new NetworkLogPlayer with a given series of log messages
     * 
     * @param messages
     *            List of NwtworkLogMessages
     * @param processor
     *            Processor of the read messages
     */
    public NetworkLogPlayer( final List<NetworkLogMessage> messages,
            final IMessageProcessor processor ) {
        Preconditions.checkNotNull( messages, "messages is null" );
        Preconditions.checkNotNull( processor, "processor is null" );

        this.messages = new LinkedList<NetworkLogMessage>( messages );
        this.numLogMessages = messages.size();
        this.processor = processor;
    }

    @Override
    public void run() {
        playLock.lock();
        play.set( true );

        while ( index.get() < messages.size() ) {
            if( !play.get() )
                break;

            NetworkLogMessage msg = messages.get( index.get() );
            processor.process( msg );
            index.incrementAndGet();

            try {
                Thread.sleep( delay.get() );
            } catch ( InterruptedException e1 ) {
                // ignore
            }
        }

        if( index.get() == messages.size() )
            processor.finished();

        play.set( false );
        playLock.unlock();
    }

    /**
     * Reset the player
     */
    public void reset() {
        stop();
        index.set( 0 );
    }

    /**
     * Start the player
     */
    public void play() {
        playLock.lock();
        new Thread( this ).start();
        playLock.unlock();
    }

    /**
     * Stop the player
     */
    public void stop() {
        play.set( false );
    }

    /**
     * Set the delay between two read messages
     * 
     * @param delay
     *            Message delay, have to be greater or equal zero
     */
    public void setDelay( final int delay ) {
        Preconditions.checkArgument( 0 <= delay,
                "Delay have to be greater or equal zero" );
        this.delay.set( delay );
    }

    public int getNumLogMessages() {
        return numLogMessages;
    }

    public int getCurrentLogMessageIndex() {
        return index.get();
    }
}
