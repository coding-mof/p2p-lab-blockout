package org.blockout.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Preconditions;

public class NetworkLogMessage {
    static SimpleDateFormat dateFormat = new SimpleDateFormat(
                                               "yyyy.MM.dd HH:mm:ss.SSS Z" );
    
    public static NetworkLogMessage parse( final String line ) {
        Preconditions.checkNotNull( line );

        if( line.isEmpty() )
            return null;

        String[] split = line.split( ";" );
        Map<String, Object> extras = new HashMap<String, Object>();
        long timestamp = -1;

        try {
            timestamp = dateFormat.parse( split[0] ).getTime();
        } catch ( ParseException e ) {
            return null;
        }
        
        for ( int i = 1; i < split.length; i++ ) {
            String[] extra = split[i].split( ":" );
            if( 2 == extra.length ) {
                extras.put( extra[0], extra[1] );
            }
        }
        
        return new NetworkLogMessage( timestamp, extras );
    }

    private long                timestamp;
    private Map<String, Object> extras;

    public NetworkLogMessage() {
        this.timestamp = System.currentTimeMillis();
        this.extras = new HashMap<String, Object>();
    }

    public NetworkLogMessage( final long timestamp ) {
        this.timestamp = timestamp;
        this.extras = new HashMap<String, Object>();
    }

    public NetworkLogMessage( final long timestamp,
            final Map<String, Object> extras ) {
        Preconditions.checkNotNull( extras );

        this.timestamp = timestamp;
        this.extras = new HashMap<String, Object>( extras );
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean hasExtra( final String key ) {
        return extras.containsKey( key );
    }

    public Object getExtra( final String key ) {
        return extras.get( key );
    }

    public void setExtra( final String key, final Object value ) {
        extras.put( key, value );
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append( dateFormat.format( new Date( timestamp ) ) );

        for ( Entry<String, Object> entry : extras.entrySet() ) {
            builder.append( ";" ).append( entry.getKey() ).append( ":" )
                    .append( entry.getValue() );
        }

        return builder.toString();
    }
}
