package org.blockout.network;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

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
    private FileWriter    logWriter;

    private IChordOverlay chordOverlay;
    private IHash         localChordId;

    public ConnectionDebugLogger( final ConnectionManager connectionMgr,
            final IChordOverlay chordOverlay ) {
        Preconditions.checkNotNull( connectionMgr );

        this.chordOverlay = chordOverlay;
        this.chordOverlay.addChordListener( this );

        localChordId = chordOverlay.getLocalId();
        connectionMgr.addConnectionListener( this );

        try {
            File logFile = new File( localChordId.getValue() + ".log" );
            if( !logFile.exists() )
                logFile.createNewFile();

            logWriter = new FileWriter( logFile );
        } catch ( IOException e ) {
            throw new RuntimeException( "failed to open connectionlog file" );
        }
    }

    @Override
    public void connected( IConnectionManager connectionMgr,
            SocketAddress localAddress, SocketAddress remoteAddress ) {
        NetworkLogMessage msg = new NetworkLogMessage();
        msg.setExtra( "id", localChordId.getValue() );
        msg.setExtra( "net", "connect" );
        msg.setExtra( "initiator", "local" );

        InetSocketAddress localAddr = (InetSocketAddress) localAddress;
        InetSocketAddress remoteAddr = (InetSocketAddress) remoteAddress;
        msg.setExtra( "localaddr", localAddr.getAddress().getHostAddress()
                + "/" + localAddr.getPort() );
        msg.setExtra( "remoteaddr", remoteAddr.getAddress().getHostAddress()
                + "/" + remoteAddr.getPort() );

        try {
            logWriter.append( msg.toString() + "\n" );
            logWriter.flush();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnected( IConnectionManager connectionMgr,
            SocketAddress localAddress, SocketAddress remoteAddress ) {
        NetworkLogMessage msg = new NetworkLogMessage();
        msg.setExtra( "id", localChordId.getValue() );
        msg.setExtra( "net", "disconnect" );
        msg.setExtra( "initiator", "local" );

        InetSocketAddress localAddr = (InetSocketAddress) localAddress;
        InetSocketAddress remoteAddr = (InetSocketAddress) remoteAddress;
        msg.setExtra( "localaddr", localAddr.getAddress().getHostAddress()
                + "/" + localAddr.getPort() );
        msg.setExtra( "remoteaddr", remoteAddr.getAddress().getHostAddress()
                + "/" + remoteAddr.getPort() );

        try {
            logWriter.append( msg.toString() + "\n" );
            logWriter.flush();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void clientConnected( IConnectionManager connectionMgr,
            SocketAddress localAddress, SocketAddress remoteAddress ) {

        NetworkLogMessage msg = new NetworkLogMessage();
        msg.setExtra( "id", localChordId.getValue() );
        msg.setExtra( "net", "connect" );
        msg.setExtra( "initiator", "remote" );

        InetSocketAddress localAddr = (InetSocketAddress) localAddress;
        InetSocketAddress remoteAddr = (InetSocketAddress) remoteAddress;
        msg.setExtra( "localaddr", localAddr.getAddress().getHostAddress()
                + "/" + localAddr.getPort() );
        msg.setExtra( "remoteaddr", remoteAddr.getAddress().getHostAddress()
                + "/" + remoteAddr.getPort() );

        try {
            logWriter.append( msg.toString() + "\n" );
            logWriter.flush();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void clientDisconnected( IConnectionManager connectionMgr,
            SocketAddress localAddress, SocketAddress remoteAddress ) {
        NetworkLogMessage msg = new NetworkLogMessage();
        msg.setExtra( "id", localChordId.getValue() );
        msg.setExtra( "net", "connect" );
        msg.setExtra( "initiator", "remote" );

        InetSocketAddress localAddr = (InetSocketAddress) localAddress;
        InetSocketAddress remoteAddr = (InetSocketAddress) remoteAddress;
        msg.setExtra( "localaddr", localAddr.getAddress().getHostAddress()
                + "/" + localAddr.getPort() );
        msg.setExtra( "remoteaddr", remoteAddr.getAddress().getHostAddress()
                + "/" + remoteAddr.getPort() );

        try {
            logWriter.append( msg.toString() + "\n" );
            logWriter.flush();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void responsibilityChanged( IChordOverlay chord,
            WrappedRange<IHash> from, WrappedRange<IHash> to ) {
        NetworkLogMessage resp = new NetworkLogMessage();
        resp.setExtra( "chord", "responsibilityChanged" );
        resp.setExtra( "id", localChordId.getValue() );
        resp.setExtra( "old", "[" + from.getLowerBound().getValue() + ","
                + from.getUpperBound().getValue() + "]" );
        resp.setExtra( "new", "[" + to.getLowerBound().getValue() + ","
                + to.getUpperBound().getValue() + "]" );

        try {
            logWriter.append( resp.toString() + "\n" );
            logWriter.flush();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void receivedMessage( IChordOverlay chord, Object message,
            IHash senderId ) {
        NetworkLogMessage msg = new NetworkLogMessage();
        msg.setExtra( "chord", "messageReceived" );
        msg.setExtra( "id", localChordId.getValue() );
        msg.setExtra( "sender", senderId.getValue() );

        try {
            logWriter.append( msg.toString() + "\n" );
            logWriter.flush();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void predecessorChanged( IChordOverlay chord, IHash predecessor ) {
        NetworkLogMessage pred = new NetworkLogMessage();
        pred.setExtra( "chord", "predecessor" );
        pred.setExtra( "id", localChordId.getValue() );
        pred.setExtra( "predid", predecessor.getValue() );

        try {
            logWriter.append( pred.toString() + "\n" );
            logWriter.flush();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void successorChanged( IChordOverlay chord, IHash successor ) {
        NetworkLogMessage succ = new NetworkLogMessage();
        succ.setExtra( "chord", "successor" );
        succ.setExtra( "id", localChordId.getValue() );
        succ.setExtra( "succid", successor.getValue() );

        try {
            logWriter.append( succ.toString() + "\n" );
            logWriter.flush();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}