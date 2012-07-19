package org.blockout.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

    Lock             playLock = new ReentrantLock();
    NetworkLogReader reader;
    AtomicBoolean    play     = new AtomicBoolean();
    AtomicInteger    delay    = new AtomicInteger();
    IMessageProcessor processor;

    public NetworkLogPlayer( final File file, final IMessageProcessor processor )
            throws FileNotFoundException {
        Preconditions.checkNotNull( file );
        Preconditions.checkNotNull( processor );

        this.processor = processor;
        reader = new NetworkLogReader( new FileReader( file ) );
        try {
            reader.mark( 0 );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        playLock.lock();
        play.set( true );

        NetworkLogMessage msg = null;
        try {
            msg = reader.readMessage();
        } catch ( IOException e1 ) {
            e1.printStackTrace();
        }

        while ( null != msg ) {
            if( !play.get() )
                break;

            processor.process( msg );

            try {
                Thread.sleep( delay.get() );
            } catch ( InterruptedException e1 ) {
                // ignore
            }

            try {
                msg = reader.readMessage();
            } catch ( IOException e ) {
                e.printStackTrace();
                break;
            }
        }

        play.set( false );
        playLock.unlock();
    }

    public void reset() throws IOException {
        stop();

        playLock.lock();
        reader.reset();
        playLock.unlock();
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
