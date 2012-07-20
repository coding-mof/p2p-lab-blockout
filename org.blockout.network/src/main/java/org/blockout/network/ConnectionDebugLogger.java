package org.blockout.network;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	private FileWriter			logWriter;

	private final IChordOverlay	chordOverlay;
	private final IHash			localChordId;
	private IHash				currentPredecessor;
	private IHash				currentSuccessor;

	public ConnectionDebugLogger(final ConnectionManager connectionMgr, final IChordOverlay chordOverlay) {
		Preconditions.checkNotNull( connectionMgr );

		this.chordOverlay = chordOverlay;
		this.chordOverlay.addChordListener( this );

		localChordId = chordOverlay.getLocalId();
		currentPredecessor = chordOverlay.getPredecessor();
		currentSuccessor = chordOverlay.getSuccessor();

		connectionMgr.addConnectionListener( this );

		try {
			File logFile = new File( localChordId.getValue() + ".log" );
			if ( !logFile.exists() ) {
				logFile.createNewFile();
			}

			logWriter = new FileWriter( logFile );

		} catch ( IOException e ) {
			throw new RuntimeException( "failed to open connectionlog file" );
		}
	}

	private void updateNeighborhoodIfChanged() {
		if ( null == currentPredecessor || !currentPredecessor.equals( chordOverlay.getPredecessor() ) ) {
			currentPredecessor = chordOverlay.getPredecessor();

			if ( null != currentPredecessor ) {
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

		if ( null == currentSuccessor || !currentSuccessor.equals( chordOverlay.getSuccessor() ) ) {
			currentSuccessor = chordOverlay.getSuccessor();

			if ( null != currentSuccessor ) {
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
	public void connected( final IConnectionManager connectionMgr, final SocketAddress localAddress,
			final SocketAddress remoteAddress ) {
		updateNeighborhoodIfChanged();
	}

	@Override
	public void disconnected( final IConnectionManager connectionMgr, final SocketAddress localAddress,
			final SocketAddress remoteAddress ) {
		updateNeighborhoodIfChanged();
	}

	@Override
	public void clientConnected( final IConnectionManager connectionMgr, final SocketAddress localAddress,
			final SocketAddress remoteAddress ) {
		updateNeighborhoodIfChanged();
	}

	@Override
	public void clientDisconnected( final IConnectionManager connectionMgr, final SocketAddress localAddress,
			final SocketAddress remoteAddress ) {
		updateNeighborhoodIfChanged();
	}

	@Override
	public void responsibilityChanged( final IChordOverlay chord, final WrappedRange<IHash> from,
			final WrappedRange<IHash> to ) {
		try {
			NetworkLogMessage msg = new NetworkLogMessage();
			msg.setExtra( "chord", "responsibilityChanged" );
			msg.setExtra( "id", localChordId.getValue() );
			msg.setExtra( "old", "[" + from.getLowerBound().getValue() + "," + from.getUpperBound().getValue() + "]" );
			msg.setExtra( "new", "[" + to.getLowerBound().getValue() + "," + to.getUpperBound().getValue() + "]" );

			logWriter.append( msg.toString() + "\n" );
			logWriter.flush();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	@Override
	public void receivedMessage( final IChordOverlay chord, final Object message, final IHash senderId ) {
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
