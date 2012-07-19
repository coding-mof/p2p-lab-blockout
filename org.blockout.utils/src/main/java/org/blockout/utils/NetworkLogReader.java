package org.blockout.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import org.blockout.common.NetworkLogMessage;

/**
 * Class to read several message from a blockout network log
 * 
 * @author Florian Müller
 * 
 */
public class NetworkLogReader extends BufferedReader {

    /**
     * Constructor to create a new {@link NetworkLogReader} to read messages
     * from the given log-file
     * 
     * @param in
     *            A {@link Reader}
     */
    public NetworkLogReader( Reader in ) {
        super( in );
    }

    /**
     * Read the next message from the log-file
     * 
     * @return Returns the next message or null if the end of the stream has
     *         been reached
     * 
     * @throws IOException
     *             Thrown if there is a problem while reading the log file
     */
    public NetworkLogMessage readMessage() throws IOException {
        if( !ready() )
            return null;

        String line = readLine();

        if( null == line )
            return null;

        return NetworkLogMessage.parse( line );
    }
}
