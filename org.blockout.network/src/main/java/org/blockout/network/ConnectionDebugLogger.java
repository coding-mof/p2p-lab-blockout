package org.blockout.network;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;

import org.blockout.common.NetworkLogMessage;
import org.blockout.network.dht.IHash;
import org.blockout.network.dht.WrappedRange;
import org.blockout.network.reworked.ChordListener;
import org.blockout.network.reworked.ConnectionListener;
import org.blockout.network.reworked.ConnectionManager;
import org.blockout.network.reworked.IChordOverlay;
import org.blockout.network.reworked.IConnectionManager;

import com.google.common.base.Preconditions;

/**
 * ConnectionDebugLogger logs all incomming and outgoing connection to a file
 * for debug purposes
 * 
 * @author Florian Müller
 * 
 */
public class ConnectionDebugLogger implements ConnectionListener, ChordListener {
    private FileWriter       logWriter;
    private SimpleDateFormat dateFormat;

    private IChordOverlay    chordOverlay;
    private IHash            localChordId;
    private IHash            currentPredecessor;
    private IHash            currentSuccessor;

    public ConnectionDebugLogger( final ConnectionManager connectionMgr,
            final IChordOverlay chordOverlay ) {
        Preconditions.checkNotNull( connectionMgr );

        this.chordOverlay = chordOverlay;
        this.chordOverlay.addChordListener( this );

        localChordId = chordOverlay.getLocalId();
        currentPredecessor = chordOverlay.getPredecessor();
        currentSuccessor = chordOverlay.getSuccessor();

        connectionMgr.addConnectionListener( this );
        dateFormat = new SimpleDateFormat( "yyyy.MM.dd HH:mm:ss.SSS Z" );

        try {
            File logFile = new File( localChordId.getValue() + ".log" );
            if( !logFile.exists() )
                logFile.createNewFile();

            logWriter = new FileWriter( logFile );

            // NetworkLogMessage msg = new NetworkLogMessage();
            // msg.setExtra( "chord", "init" );
            // msg.setExtra( "id", localChordId.getValue() );
            //
            // logWriter.append( msg.toString() + "\n" );
            // logWriter.flush();

        } catch ( IOException e ) {
            throw new RuntimeException( "failed to open connectionlog file" );
        }
    }

    private void updateNeighborhoodIfChanged() {
        if( null == currentPredecessor
                || !currentPredecessor.equals( chordOverlay.getPredecessor() ) ) {
            currentPredecessor = chordOverlay.getPredecessor();

            if( null != currentPredecessor ) {
                try {
                    NetworkLogMessage msg = new NetworkLogMessage();
                    msg.setExtra( "chord", "predecessor" );
                    msg.setExtra( "id", localChordId.getValue() );
                    msg.setExtra( "predid", currentPredecessor.getValue() );

                    logWriter.append( msg.toString() + "\n" );
                    logWriter.flush();
                } catch ( IOException e ) {
                    e.printStackTrace();
                }
            }
        }

        if( null == currentSuccessor
                || !currentSuccessor.equals( chordOverlay.getSuccessor() ) ) {
            currentSuccessor = chordOverlay.getSuccessor();

            if( null != currentSuccessor ) {
                try {
                    NetworkLogMessage msg = new NetworkLogMessage();
                    msg.setExtra( "chord", "successor" );
                    msg.setExtra( "id", localChordId.getValue() );
                    msg.setExtra( "succid", currentSuccessor.getValue() );

                    logWriter.append( msg.toString() + "\n" );
                    logWriter.flush();
                } catch ( IOException e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void connected( IConnectionManager connectionMgr,
            SocketAddress localAddress, SocketAddress remoteAddress ) {
        updateNeighborhoodIfChanged();
    }

    @Override
    public void disconnected( IConnectionManager connectionMgr,
            SocketAddress localAddress, SocketAddress remoteAddress ) {
        updateNeighborhoodIfChanged();
    }

    @Override
    public void clientConnected( IConnectionManager connectionMgr,
            SocketAddress localAddress, SocketAddress remoteAddress ) {
        updateNeighborhoodIfChanged();
    }

    @Override
    public void clientDisconnected( IConnectionManager connectionMgr,
            SocketAddress localAddress, SocketAddress remoteAddress ) {
        updateNeighborhoodIfChanged();
    }

    @Override
    public void responsibilityChanged( IChordOverlay chord,
            WrappedRange<IHash> from, WrappedRange<IHash> to ) {
        try {
            NetworkLogMessage msg = new NetworkLogMessage();
            msg.setExtra( "chord", "responsibilityChanged" );
            msg.setExtra( "id", localChordId.getValue() );
            msg.setExtra( "old", "[" + from.getLowerBound().getValue() + ","
                    + from.getUpperBound().getValue() + "]" );
            msg.setExtra( "new", "[" + to.getLowerBound().getValue() + ","
                    + to.getUpperBound().getValue() + "]" );
            
            logWriter.append( msg.toString() + "\n" );
            logWriter.flush();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void receivedMessage( IChordOverlay chord, Object message,
            IHash senderId ) {
        try {
            NetworkLogMessage msg = new NetworkLogMessage();
            msg.setExtra( "chord", "messageReceived" );
            msg.setExtra( "id", localChordId.getValue() );
            msg.setExtra( "sender", senderId.getValue() );

            logWriter.append( msg.toString() + "\n" );
            logWriter.flush();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}
