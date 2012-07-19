package org.blockout.common;
import junit.framework.Assert;

import org.junit.Test;


public class NetworkLogMessageTest {

    @Test( expected = NullPointerException.class )
    public void testConstructorNull() {
        new NetworkLogMessage( -1, null );
    }

    @Test
    public void testParseEmptyString() {
        Assert.assertNull( NetworkLogMessage.parse( "" ) );
    }

    @Test( expected = NullPointerException.class )
    public void testParseNull() {
        NetworkLogMessage.parse( null );
    }

    @Test
    public void testParseValid() {
        String line = "2012.07.19 14:08:52.358 +0200;id:f5eeeb91f093ce7496afa3e95dae5ee070be31c2;chord:predecessor;predid:f5eeeb91f093ce7496afa3e95dae5ee070be31c2";

        NetworkLogMessage msg = NetworkLogMessage.parse( line );

        System.out.println( line );
        System.out.println( msg );

        Assert.assertTrue( 0 <= msg.getTimestamp() );
        Assert.assertTrue( msg.hasExtra( "id" ) );
        Assert.assertTrue( msg.hasExtra( "chord" ) );
        Assert.assertTrue( msg.hasExtra( "predid" ) );
    }
}
