package org.blockout.network;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.blockout.network.reworked.ConnectionListener;
import org.blockout.network.reworked.ConnectionManager;
import org.blockout.network.reworked.IConnectionManager;

import com.google.common.base.Preconditions;

/**
 * ConnectionDebugLogger logs all incomming and outgoing connection to a file
 * for debug purposes
 * 
 * @author Florian Müller
 * 
 */
public class ConnectionDebugLogger implements ConnectionListener {
    private FileWriter       logWriter;
    private SimpleDateFormat dateFormat;

    public ConnectionDebugLogger( final ConnectionManager connectionMgr ) {
        Preconditions.checkNotNull( connectionMgr );

        connectionMgr.addConnectionListener( this );
        dateFormat = new SimpleDateFormat( "yyyy.MM.dd HH:mm:ss.SSS Z" );

        try {
            File logFile = new File( new SimpleDateFormat(
                    "yyyy.MM.dd-HHmmssSSS" ).format( new Date() ) + ".log" );
            if( !logFile.exists() )
                logFile.createNewFile();

            logWriter = new FileWriter( logFile );

            logWriter
                    .append( "#=======================================================\n" );
            logWriter.append( "#Start new connection log @ "
                    + dateFormat.format( new Date() ) + "\n" );
            logWriter
                    .append( "#=======================================================\n" );
        } catch ( IOException e ) {
            throw new RuntimeException( "failed to open connectionlog file" );
        }
    }

    @Override
    public void connected( IConnectionManager connectionMgr,
            SocketAddress localAddress, SocketAddress remoteAddress ) {

        try {
            logWriter.append( dateFormat.format( new Date() )
                    + " - outgoing connected - localaddr: " + localAddress
                    + " remoteaddr: " + remoteAddress + "\n" );
            logWriter.flush();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnected( IConnectionManager connectionMgr,
            SocketAddress localAddress, SocketAddress remoteAddress ) {
        try {
            logWriter.append( dateFormat.format( new Date() )
                    + " - outgoing disconnected - localaddr: " + localAddress
                    + " remoteaddr: " + remoteAddress + "\n" );
            logWriter.flush();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void clientConnected( IConnectionManager connectionMgr,
            SocketAddress localAddress, SocketAddress remoteAddress ) {
        try {
            logWriter.append( dateFormat.format( new Date() )
                    + " - incoming connected - localaddr: " + localAddress
                    + " remoteaddr: " + remoteAddress + "\n" );
            logWriter.flush();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void clientDisconnected( IConnectionManager connectionMgr,
            SocketAddress localAddress, SocketAddress remoteAddress ) {
        try {
            logWriter.append( dateFormat.format( new Date() )
                    + " - incoming disconnected - localaddr: " + localAddress
                    + " remoteaddr: " + remoteAddress + "\n" );
            logWriter.flush();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}
