package org.blockout.logic;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class FogOfWar {

	private static final int		SEGMENT_SIZE	= 48;

	protected Map<String, BitSet>	exploreState;

	public FogOfWar() {
		exploreState = new HashMap<String, BitSet>();
	}

	public boolean isExplored( final int x, final int y ) {
		int segmentX = x / SEGMENT_SIZE;
		int segmentY = y / SEGMENT_SIZE;
		BitSet segment = exploreState.get( "(" + segmentX + "," + segmentY + ")" );
		if ( segment == null ) {
			return false;
		}
		int ofsetX = x % SEGMENT_SIZE;
		int ofsetY = y % SEGMENT_SIZE;
		return segment.get( ofsetX * SEGMENT_SIZE + ofsetY );
	}

	public void setExplored( final int x, final int y, final boolean explored ) {
		int segmentX = x / SEGMENT_SIZE;
		int segmentY = y / SEGMENT_SIZE;
		String key = "(" + segmentX + "," + segmentY + ")";
		BitSet segment = exploreState.get( key );
		if ( segment == null ) {
			segment = new BitSet( SEGMENT_SIZE * SEGMENT_SIZE );
			exploreState.put( key, segment );
		}
		int ofsetX = x % SEGMENT_SIZE;
		int ofsetY = y % SEGMENT_SIZE;
		segment.set( ofsetX * SEGMENT_SIZE + ofsetY, explored );
	}
}
